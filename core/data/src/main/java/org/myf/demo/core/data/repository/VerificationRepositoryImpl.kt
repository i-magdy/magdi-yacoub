package org.myf.demo.core.data.repository


import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import org.myf.demo.core.common.annotation.Dispatcher
import org.myf.demo.core.common.annotation.Google
import org.myf.demo.core.common.annotation.MyDispatchers.IO
import org.myf.demo.core.common.uiState.VerifyUiState
import org.myf.demo.core.common.util.VerifyUiError
import org.myf.demo.core.datastore.DatastoreImpl
import org.myf.demo.core.datastore.PatientDataRepo
import org.myf.demo.core.model.countries.CountryCodeModel
import javax.inject.Inject

class VerificationRepositoryImpl @Inject constructor(
    private val countriesRepo : CountriesRepository,
    @Google private val verificationService: VerificationServiceRepository,
    private val datastore: DatastoreImpl,
    private val patientRepo: PatientDataRepo,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
): VerificationRepository {

    private var _appLang = ""
    override var appLanguage: String
        get() = _appLang
        set(value) {
            if (value.isNotEmpty()) {
                _appLang = value
            }
        }

    override val uiState: MutableStateFlow<VerifyUiState> = MutableStateFlow(
        VerifyUiState()
    )

    private var countries: List<CountryCodeModel> = emptyList()
    private val coroutine = CoroutineScope(ioDispatcher + SupervisorJob())

    init {
        coroutine.launch {
            verificationService.state.collect{ state ->
                //update ui when code is requested
                if (state.codeRequested){
                    uiState.getAndUpdate {
                        it.copy(
                            isCodeRequested = true,
                            error = VerifyUiError.NONE
                        )
                    }
                }
                //update ui when code is sent to user
                uiState.getAndUpdate {
                    it.copy(isCodeSent = state.codeSent)
                }
                //update ui when phone number is wrong
                if (state.wrongPhone){
                    uiState.getAndUpdate {
                        it.copy(
                            isCodeRequested = false,
                            error = VerifyUiError.INVALID_PHONE
                        )
                    }
                }
                //update ui if code is wrong
                if (state.wrongCode){
                    uiState.getAndUpdate {
                        it.copy(
                            isVerifying = false,
                            error = VerifyUiError.INVALID_CODE
                        )
                    }
                }
                //update ui when succeed verify
                if (state.isSucceed){
                    succeed(
                        isSucceed = true,
                        forLogin = uiState.value.shouldLogin
                    )
                }

            }
        }
    }

    override suspend fun init(
        lang: String,
        builder: PhoneAuthOptions.Builder
    ) {
        appLanguage = lang
        verificationService.initService(
            appLanguage = appLanguage,
            builder = builder
        )
    }

    override suspend fun getCountryByCode(
        countryCode: String
    ) {
        val country = countriesRepo.getCountryByCode(countryCode)
        if (country != null) {
            uiState.getAndUpdate {
                it.copy(selectedCountry = country)
            }
            filterPhoneNumber()
        }
    }

    override suspend fun getCountries() {
        countries = countriesRepo.getCountries()
        val list = ArrayList<String>()
        if (_appLang == "ar"){
            countries = countries.sortedBy { it.ar_name }
            countries.forEach { list.add(it.ar_name) }
        }else{
            countries = countries.sortedBy { it.en_name }
            countries.forEach { list.add(it.en_name) }
        }
        uiState.getAndUpdate {
            it.copy(countries = list)
        }
    }

    override suspend fun setPhoneNumber(
        phone: String
    ) {
        val p = uiState.value.phone
        if (p != phone){
            uiState.getAndUpdate {
                it.copy(phone = phone)
            }
            filterPhoneNumber()
        }
    }

    private fun filterPhoneNumber(){
        val p = uiState.value.phone
        val country = uiState.value.selectedCountry
        uiState.getAndUpdate {
            it.copy(phone = p.replace(country.code,""))
        }
    }

    override suspend fun selectCountryByName(
        countryName: String
    ) {
        coroutine.launch {
            if (uiState.value.selectedCountry.code != "ــ") {
                if (_appLang == "ar") {
                    if (uiState.value.selectedCountry.ar_name == countryName) {
                        return@launch
                    }
                } else {
                    if (uiState.value.selectedCountry.en_name == countryName) {
                        return@launch
                    }
                }
                countries.forEach {
                    if (_appLang == "ar") {
                        if (it.ar_name == countryName) {
                            setSelectedCountry(it)
                        }
                    } else {
                        if (it.en_name == countryName) {
                            setSelectedCountry(it)
                        }
                    }
                }
            }
        }
    }

    override suspend fun requestVerificationCode() {
        val phone = uiState.value.phone
        if (phone.isNotBlank() && phone.length > 7) {
            verificationService.requestCode(uiState.value.selectedCountry.code + phone)
        }
    }

    override suspend fun verifyPhoneNumber(
        verificationCode: String
    ) {
        verificationService.verify(verificationCode)
        uiState.getAndUpdate {
            it.copy(isVerifying = true)
        }
    }

    override suspend fun succeed(
        isSucceed: Boolean,
        forLogin: Boolean
    ) {
        if (isSucceed.and(forLogin)){
            updateUiStateForLogin()
        }
        uiState.getAndUpdate {
            it.copy(
                isSuccess = isSucceed,
                isCodeSent = true,
                isCodeRequested = true
            )
        }
        val user = Firebase.auth.currentUser
        savePatientData(
            verified = user != null,
            primaryPhone = user?.phoneNumber ?: ""
        )
    }
    override suspend fun clearError() {
        uiState.getAndUpdate {
            it.copy(error = VerifyUiError.NONE)
        }
    }

    override suspend fun savePatientData(
        primaryPhone: String,
        verified: Boolean
    ) {
        patientRepo.updatePatientDataOnVerifyScreen(
            primaryPhone = primaryPhone,
            verified = verified
        )
    }

    private fun setSelectedCountry(
        country: CountryCodeModel
    ) {
        uiState.getAndUpdate {
            it.copy(selectedCountry = country)
        }
    }

    override suspend fun updateUiForLogin() {
        uiState.getAndUpdate {
            it.copy(shouldLogin = true)
        }
    }

    override suspend fun updateUiStateForLogin() {
        datastore.updateState(2)
    }

    override fun cancelJob() {
        coroutine.cancel()
        verificationService.cancelJob()
    }
}