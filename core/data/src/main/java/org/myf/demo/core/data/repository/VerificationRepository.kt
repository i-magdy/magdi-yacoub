package org.myf.demo.core.data.repository

import com.google.firebase.auth.PhoneAuthOptions
import kotlinx.coroutines.flow.MutableStateFlow
import org.myf.demo.core.common.uiState.VerifyUiState

interface VerificationRepository {

    var appLanguage: String
    val uiState: MutableStateFlow<VerifyUiState>
    suspend fun init(lang: String,builder: PhoneAuthOptions.Builder)
    suspend fun getCountryByCode(countryCode: String)
    suspend fun getCountries()
    suspend fun setPhoneNumber(phone: String)
    suspend fun selectCountryByName(countryName: String)
    suspend fun requestVerificationCode()
    suspend fun verifyPhoneNumber(verificationCode: String)
    suspend fun succeed(isSucceed: Boolean, forLogin: Boolean)
    suspend fun clearError()
    suspend fun updateUiForLogin()
    suspend fun updateUiStateForLogin()
    suspend fun savePatientData(primaryPhone: String, verified: Boolean)
    fun cancelJob()
}