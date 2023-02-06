package org.myf.demo.core.common.uiState

import android.net.Uri
import org.myf.demo.core.common.util.CreatePatientUiError

data class CreatePatientUiState (
    val patientName: String = "",
    val nationalId: String = "",
    val nationalIdImage: String = "",
    val isNameRequired: Boolean = true,
    val isNationalIdRequired: Boolean = true,
    val isNationalIdImageRequired: Boolean = true,
    val isVerifying: Boolean = false,
    val isSuccess: Boolean = false,
    val img: Uri = Uri.parse(""),
    val email: String = "",
    val nameError: CreatePatientUiError = CreatePatientUiError.NONE,
    val idError: CreatePatientUiError = CreatePatientUiError.NONE,
    val emailError: CreatePatientUiError = CreatePatientUiError.NONE
)
