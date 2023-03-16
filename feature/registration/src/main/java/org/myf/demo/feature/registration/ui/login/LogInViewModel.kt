package org.myf.demo.feature.registration.ui.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import org.myf.demo.core.common.model.CurrentPatientModel
import org.myf.demo.core.data.repository.CurrentPatientRepository
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val repository: CurrentPatientRepository
): ViewModel(){

    val patient: StateFlow<CurrentPatientModel> = repository.patient

    suspend fun getPatient(
        id: String
    ): Boolean = repository.getPatientAsync(
        id = id
    ).await()

    override fun onCleared() {
        super.onCleared()
        repository.cancelJob()
    }
}