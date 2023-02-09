package org.myf.demo.feature.registration.ui.verify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.myf.demo.core.common.uiState.VerifyUiState
import org.myf.demo.core.data.repository.VerificationRepository
import javax.inject.Inject

@HiltViewModel
class VerifyViewModel @Inject constructor(
    private val repo: VerificationRepository
): ViewModel() {

    val uiState: StateFlow<VerifyUiState> = repo.uiState

    fun shouldUpdateUiForLogIn() = viewModelScope.launch {
        repo.updateUiForLogin()
    }

    fun getCountries() = viewModelScope.launch {
        repo.getCountries()
    }

    fun getCountryCode(
        country: String
    ) = viewModelScope.launch {
        repo.getCountryByCode(country)
    }

    fun setCountry(
        name: String
    ) = viewModelScope.launch {
        repo.selectCountryByName(name)
    }

    fun setPhone(
        phone: String
    ) = viewModelScope.launch {
       repo.setPhoneNumber(phone)
    }


    fun requestCode() = viewModelScope.launch {
        repo.requestVerificationCode()
    }

    fun savePatient(
        phone: String,
        verified: Boolean
    ) = viewModelScope.launch {
        repo.savePatientData(
            primaryPhone = phone,
            verified = verified
        )
    }

    fun verify(
        code: String?
    ) = viewModelScope.launch {
        if (code != null){
            if (code.isNotBlank() && code.length == 6){
               repo.verifyPhoneNumber(code)
            }
        }
    }

    fun init(
        lang: String,
        builder: PhoneAuthOptions.Builder
    ) = viewModelScope.launch {
        repo.init(
            lang = lang,
            builder = builder
        )
    }


    fun clearError() = viewModelScope.launch {
        repo.clearError()
    }

    fun succeed(
        isSucceed: Boolean,
        forLogin: Boolean
    ) = viewModelScope.launch {
       repo.succeed(
           isSucceed = isSucceed,
           forLogin = forLogin
       )
    }

    override fun onCleared() {
        super.onCleared()
        repo.cancelJob()
        viewModelScope.coroutineContext.cancelChildren()
    }
}