package org.myf.demo.core.data.repository

import com.google.firebase.auth.PhoneAuthOptions
import kotlinx.coroutines.flow.MutableStateFlow
import org.myf.demo.core.common.uiState.VerificationServiceState

interface VerificationServiceRepository {
    val state: MutableStateFlow<VerificationServiceState>
    fun initService(appLanguage: String = "ar",builder: PhoneAuthOptions.Builder)
    fun requestCode(phone: String)
    fun verify(code: String)
    fun cancelJob()
}