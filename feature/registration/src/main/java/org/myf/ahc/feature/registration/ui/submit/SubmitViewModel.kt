package org.myf.ahc.feature.registration.ui.submit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.myf.ahc.core.datastore.PatientData
import javax.inject.Inject

@HiltViewModel
class SubmitViewModel @Inject constructor(
    private val repo: SubmitRepo
): ViewModel(){


    val patient: StateFlow<PatientData?> = repo.patient


    fun syncPatientData() = viewModelScope.launch {
        repo.syncPatientData()
    }


}