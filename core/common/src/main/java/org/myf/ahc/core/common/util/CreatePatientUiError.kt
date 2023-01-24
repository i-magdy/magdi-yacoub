package org.myf.ahc.core.common.util

object PatientBasicInfo{
    const val invalid_patientName: String = "The provided patient name is incorrect. Please provide patient name written in the format [First name][Second name][Last name]."
    const val invalid_nationalId: String = "The provided national id is incorrect. Please provide national id of length [14]."
}
enum class CreatePatientUiError {
    NONE,INVALID_PATIENT_NAME,INVALID_NATIONAL_ID
}