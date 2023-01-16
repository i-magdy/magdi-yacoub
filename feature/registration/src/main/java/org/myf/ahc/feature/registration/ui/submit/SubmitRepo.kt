package org.myf.ahc.feature.registration.ui.submit

import kotlinx.coroutines.flow.MutableStateFlow
import org.myf.ahc.core.datastore.PatientDataRepo
import org.myf.ahc.core.datastore.PatientData
import javax.inject.Inject

class SubmitRepo @Inject constructor(
    private val patientRepo: PatientDataRepo
) {

    val patient = MutableStateFlow<PatientData?>(null)

    suspend fun syncPatientData(){
        patientRepo.patient.collect{
            patient.emit(it)
        }
    }


}