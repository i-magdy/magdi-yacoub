package org.myf.ahc.feature.registration.ui.createPatient

import android.net.Uri
import org.myf.ahc.feature.registration.util.CreatePatientUiError

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
