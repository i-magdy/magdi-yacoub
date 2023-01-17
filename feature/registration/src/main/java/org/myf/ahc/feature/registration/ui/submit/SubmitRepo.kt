package org.myf.ahc.feature.registration.ui.submit

import kotlinx.coroutines.flow.MutableStateFlow
import org.myf.ahc.core.datastore.PatientDataRepo
import org.myf.ahc.core.datastore.PatientModel
import javax.inject.Inject

class SubmitRepo @Inject constructor(
    private val patientRepo: PatientDataRepo
) {

    val patient = MutableStateFlow<PatientModel>(PatientModel())

    suspend fun syncPatientData(){
        patientRepo.getPatientMessage().collect{
            patient.emit(it)
        }
    }


}