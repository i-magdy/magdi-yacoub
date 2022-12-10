package org.myf.ahc.ui.registration.createPatient

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.myf.ahc.data.DatastoreImpl
import org.myf.ahc.util.CreatePatientUiError
import org.myf.ahc.util.VerifyUiError
import javax.inject.Inject

@HiltViewModel
class CreatePatientViewModel @Inject constructor(
    private val repo: CreatePatientRepo,
    private val datastore: DatastoreImpl
): ViewModel() {

    private val _uiState = MutableStateFlow(CreatePatientUiState())
    private val _patientName = MutableStateFlow("")
    private val _nationalId = MutableStateFlow("")

    val uiState: StateFlow<CreatePatientUiState> = _uiState
    val patientName: StateFlow<String> = _patientName
    val nationalId: StateFlow<String> = _nationalId

    val state = datastore.state


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

    fun updateState(i: Int) = viewModelScope.launch {
        datastore.updateState(i)
    }

    fun validateInput() {
        //TODO
    }


}