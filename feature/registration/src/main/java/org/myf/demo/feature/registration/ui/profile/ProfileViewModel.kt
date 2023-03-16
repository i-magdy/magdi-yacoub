package org.myf.demo.feature.registration.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.myf.demo.core.common.model.CurrentPatientModel
import org.myf.demo.core.data.repository.CurrentPatientRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val currentPatientRepository: CurrentPatientRepository
): ViewModel(){

    val patient: StateFlow<CurrentPatientModel> = currentPatientRepository.patient

    init {
        viewModelScope.launch {
            currentPatientRepository.getPatientAsync().await()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancelChildren()
        currentPatientRepository.cancelJob()
    }
}