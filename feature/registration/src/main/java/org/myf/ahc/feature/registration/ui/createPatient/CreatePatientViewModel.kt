package org.myf.ahc.feature.registration.ui.createPatient

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.myf.ahc.core.datastore.PatientDataRepo
import org.myf.ahc.feature.registration.util.CreatePatientUiError

import javax.inject.Inject

@HiltViewModel
class CreatePatientViewModel @Inject constructor(
    private val patientRepo: PatientDataRepo
): ViewModel() {

    private val _uiState = MutableStateFlow(CreatePatientUiState())
    val uiState: StateFlow<CreatePatientUiState> = _uiState

    fun sync() =   viewModelScope.launch {
        patientRepo.patient.collect{

            _uiState.emit(
                value = _uiState.value.copy(
                    patientName = "it.name",
                    nationalId = "it.id",
                    img = Uri.parse("it.imgUri"),
                    email = "it.email"
                )
            )
        }
    }

    fun attemptSavePatient(
        name: String,
        id: String,
        email: String
    ) = viewModelScope.launch {
        patientRepo.updatePatientDataOnCreateScreen(
            name = name,
            id = id,
            img = _uiState.value.img,
            email = email
        )
    }

    fun setImageUri(
        img: Uri
    ) = viewModelScope.launch {
        _uiState.emit(
            value = _uiState.value.copy(img = img)
        )
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