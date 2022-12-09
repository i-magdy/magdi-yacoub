package org.myf.ahc.ui.registration.verify

import org.myf.ahc.util.VerifyUiError

data class VerifyUiState(
    val phone: String = "",
    val isRequestingPhone: Boolean = false,
    val isCodeRequested: Boolean = false,
    val isCodeSent: Boolean = false,
    val isVerifying: Boolean = false,
    val isSuccess: Boolean = false,
    val error: VerifyUiError = VerifyUiError.NONE,
    val shouldLogin: Boolean = false
)
