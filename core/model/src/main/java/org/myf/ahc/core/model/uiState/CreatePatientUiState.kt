package org.myf.ahc.core.model.uiState

import android.net.Uri
import org.myf.ahc.core.common.util.CreatePatientUiError

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
    val error: CreatePatientUiError = CreatePatientUiError.NONE
)
