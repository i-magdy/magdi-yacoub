package org.myf.ahc.feature.registration.ui.verify


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.myf.ahc.core.data.repository.CountriesRepository
import org.myf.ahc.core.datastore.DatastoreImpl
import org.myf.ahc.core.datastore.PatientDataRepo
import org.myf.ahc.core.model.countries.CountryCodeModel
import javax.inject.Inject

class VerifyRepo @Inject constructor(
    private val countriesRepo : CountriesRepository,
    private val datastore: DatastoreImpl,
    private val patientRepo: PatientDataRepo
) {

    private var _appLang = ""
    private var _selectedCountry: CountryCodeModel? = null
    private var countries: List<CountryCodeModel> = emptyList()
    private val coroutine = CoroutineScope(Dispatchers.Default)
    val selectedCountry = MutableStateFlow(CountryCodeModel())
    val countriesName = MutableStateFlow<List<String>>(emptyList())

    suspend fun getCountryByCode(code: String) {
        val country = countriesRepo.getCountryByCode(code)
        if (country != null) {
            selectedCountry.emit(country)
        }
    }



    fun setSelectedCountryByName(
        name: String
    ) = coroutine.launch {
        if(_selectedCountry != null) {
            if (_appLang == "ar") {
                if (_selectedCountry!!.ar_name == name) {
                    return@launch
                }
            } else {
                if (_selectedCountry!!.en_name == name) {
                    return@launch
                }
            }
        }
        countries.forEach {
            if (_appLang == "ar") {
                if (it.ar_name == name) {
                    setSelectedCountry(it)
                }
            } else {
                if (it.en_name == name) {
                    setSelectedCountry(it)
                }
            }
        }
        selectedCountry.emit(_selectedCountry!!)
    }

    suspend fun getCountries() {
        countries = countriesRepo.getCountries()
        val list = ArrayList<String>()
        countries.forEach {
            if (_appLang == "ar"){
                list.add(it.ar_name)
            }else{
                list.add(it.en_name)
            }
        }
        countriesName.emit(list)
    }

    suspend fun savePatientData(
        primaryPhone: String,
        verified: Boolean
    ) = patientRepo.updatePatientDataOnVerifyScreen(
        primaryPhone = primaryPhone,
        verified = verified
    )

    private fun setSelectedCountry(
        country: CountryCodeModel
    ) {
        _selectedCountry = country
    }

    fun setAppLang(lang: String) {
        _appLang = lang
    }


    fun updateStateForLogin() = coroutine.launch {
        datastore.updateState(2)
    }

    fun cancelJobs() = coroutine.cancel()
}