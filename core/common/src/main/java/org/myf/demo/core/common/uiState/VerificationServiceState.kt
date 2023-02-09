package org.myf.demo.core.common.uiState

data class VerificationServiceState(
    val codeRequested: Boolean = false,
    val codeSent: Boolean = false,
    val wrongPhone: Boolean = false,
    val wrongCode: Boolean = false,
    val isSucceed: Boolean = false
)
