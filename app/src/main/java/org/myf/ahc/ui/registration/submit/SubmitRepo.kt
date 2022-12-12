package org.myf.ahc.ui.registration.submit

import kotlinx.coroutines.flow.MutableStateFlow
import org.myf.ahc.PatientData
import org.myf.ahc.repos.PatientDataRepo
import javax.inject.Inject

class SubmitRepo @Inject constructor(
    private val patientRepo: PatientDataRepo
) {

    val patient = MutableStateFlow<PatientData>(PatientData.getDefaultInstance())

    suspend fun syncPatientData(){
        patientRepo.patient.collect{
            patient.emit(it)
        }
    }


}