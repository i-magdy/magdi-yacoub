package org.myf.ahc.ui.verify

data class VerifyUiState(
    var phone: String = "",
    var isRequestingPhone: Boolean = false,
    var isCodeRequested: Boolean = false,
    var isCodeSent: Boolean = false,
    var isVerifying: Boolean = false,
    var isSuccess: Boolean = false
)
