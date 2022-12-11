package org.myf.ahc.ui.registration.createPatient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.myf.ahc.util.CreatePatientUiError
import javax.inject.Inject

@HiltViewModel
class CreatePatientViewModel @Inject constructor(
    private val repo: CreatePatientRepo,
): ViewModel() {

    private val _uiState = MutableStateFlow(CreatePatientUiState())
    val uiState: StateFlow<CreatePatientUiState> = _uiState


    fun setPatientName(
        name: String
    ) = viewModelScope.launch {
        val patientName = _uiState.value.patientName
        if (name.isNotBlank() && patientName != name) {
            _uiState.emit(_uiState.value.copy(patientName = name))
        }
    }

    fun setNationalId(
        id: String
    ) = viewModelScope.launch {
        val nationalId = _uiState.value.nationalId
        if (id.isNotBlank() && nationalId != id) {
            _uiState.emit(_uiState.value.copy(nationalId = id))
        }
    }

    fun clearError() = viewModelScope.launch {
        _uiState.emit(
            value = _uiState.value.copy(error = CreatePatientUiError.NONE)
        )
    }


    fun validateInput() {
        //TODO
    }


}