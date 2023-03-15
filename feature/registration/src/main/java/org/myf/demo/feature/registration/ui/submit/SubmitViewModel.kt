package org.myf.demo.feature.registration.ui.submit

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import org.myf.demo.core.data.repository.SubmitRepository
import org.myf.demo.core.datastore.PatientModel
import javax.inject.Inject

@HiltViewModel
class SubmitViewModel @Inject constructor(
    private val repo: SubmitRepository
): ViewModel(){

    val patient: StateFlow<PatientModel> = repo.patient

    suspend fun createPatent(): Boolean = repo.createPatientAsync().await()

    override fun onCleared() {
        super.onCleared()
        repo.cancelJob()
    }

}