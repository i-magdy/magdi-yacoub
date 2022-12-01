package org.myf.ahc.ui.registration.verify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.myf.ahc.models.CountryCodeModel
import org.myf.ahc.util.VerifyUiError
import javax.inject.Inject

@HiltViewModel
class VerifyViewModel @Inject constructor(
    private val repo: VerifyRepo
): ViewModel() {

    private val _uiState = MutableStateFlow(VerifyUiState())
    private val _phoneToVerify = MutableStateFlow("")
    private val _verifyCode = MutableStateFlow("")
    val uiState: StateFlow<VerifyUiState> = _uiState
    val selectedCountry: StateFlow<CountryCodeModel> = repo.selectedCountry
    val countriesName: StateFlow<List<String>> = repo.countriesName
    val phoneToVerify: StateFlow<String> = _phoneToVerify
    val verifyCode: StateFlow<String> = _verifyCode


    fun getCountryCode(
        country: String
    ) = viewModelScope.launch {
        repo.getCountryByCode(country)
    }

    fun setCountry(
        name: String
    ) = repo.setSelectedCountryByName(name)

    fun setPhone(
        phone: String
    ) = viewModelScope.launch {
        val mPhone = _uiState.value.phone
        if (phone.isNotBlank() && mPhone != phone) {
            _uiState.emit(_uiState.value.copy(phone = phone))
            filterPhone()
        }
    }

    fun filterPhone() = viewModelScope.launch {
        val phone = _uiState.value.phone.replace(selectedCountry.value.code,"")
        _uiState.emit(_uiState.value.copy(phone = phone))
    }

    fun requestCode() = viewModelScope.launch {
        val phone = _uiState.value.phone
        if (phone.isNotBlank()) {
                _phoneToVerify.emit(selectedCountry.value.code + phone)
        }
    }

    fun verify(
        code: String?
    ) = viewModelScope.launch {
        if (code != null){
            if (code.isNotBlank() && code.length == 6){
                _verifyCode.emit(code)
                _uiState.emit(_uiState.value.copy(isVerifying = true))
            }
        }
    }

    fun codeRequested() = viewModelScope.launch {
        _uiState.emit(_uiState.value.copy(
            isCodeRequested = true,
            error = VerifyUiError.NONE
        ))
    }

    fun codeSent(b: Boolean) = viewModelScope.launch {
        _uiState.emit(_uiState.value.copy(isCodeSent = b))
        _phoneToVerify.emit("")
    }

    fun setAppLang(lang: String) = repo.setAppLang(lang)

    fun wrongPhone() = viewModelScope.launch {
        _uiState.emit(
            value = _uiState.value.copy(
                isCodeRequested = false,
                error = VerifyUiError.INVALID_PHONE
            )
        )
        _phoneToVerify.emit("")
    }

    fun wrongCode() = viewModelScope.launch {
        _uiState.emit(
            value = _uiState.value.copy(
                isVerifying = false,
                error = VerifyUiError.INVALID_CODE
            )
        )
        _phoneToVerify.emit("")
    }

    fun clearError() = viewModelScope.launch {
        _uiState.emit(
            value = _uiState.value.copy(error = VerifyUiError.NONE)
        )
    }

    fun succeed() = viewModelScope.launch {
        _uiState.emit(
            value = _uiState.value.copy(isSuccess = true, isCodeSent = true, isCodeRequested = true)
        )
        _phoneToVerify.emit("")
    }
}