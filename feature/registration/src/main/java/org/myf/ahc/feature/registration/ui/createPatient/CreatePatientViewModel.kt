package org.myf.ahc.feature.registration.ui.createPatient

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.myf.ahc.core.common.uiState.CreatePatientUiState
import org.myf.ahc.core.common.util.CreatePatientUiError
import org.myf.ahc.core.common.util.CreatePatientUtil.isEgyptianIdValid
import org.myf.ahc.core.common.util.CreatePatientUtil.isEmailValid
import org.myf.ahc.core.common.util.CreatePatientUtil.isNameValid
import org.myf.ahc.core.datastore.PatientDataRepo
import java.util.Calendar

import javax.inject.Inject

@HiltViewModel
class CreatePatientViewModel @Inject constructor(
    private val patientRepo: PatientDataRepo
): ViewModel() {

    private val _uiState = MutableStateFlow(CreatePatientUiState())
    val uiState: StateFlow<CreatePatientUiState> = _uiState

    fun sync() = viewModelScope.launch {
        patientRepo.getPatientMessage().collect{
            _uiState.emit(
                value = _uiState.value.copy(
                    patientName = it.name,
                    nationalId = it.id,
                    img = it.img,
                    email = it.email
                )
            )
        }
    }

    fun attemptSavePatient(
        name: String,
        id: String,
        email: String
    ) = viewModelScope.launch {
        val calendar = Calendar.getInstance()
        _uiState.emit(
            value = _uiState.value.copy(
                nameError = isNameValid(name),
                idError = isEgyptianIdValid(
                    id = id,
                    currentMonthDigit = calendar[Calendar.MONTH]++,
                    currentYearDigit = calendar[Calendar.YEAR]
                ),
                emailError = isEmailValid(email)
            )
        )
        delay(50)
        val succeed = _uiState.value.nameError == CreatePatientUiError.NONE &&
                _uiState.value.idError == CreatePatientUiError.NONE &&
                _uiState.value.emailError == CreatePatientUiError.NONE
        if (succeed){
            patientRepo.updatePatientDataOnCreateScreen(
                name = name,
                id = id,
                img = _uiState.value.img,
                email = email
            )
        }
        delay(50)
        _uiState.emit(
            value = _uiState.value.copy(
                isSuccess = succeed
            )
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
            value = _uiState.value.copy(
                nameError = CreatePatientUiError.NONE,
                idError = CreatePatientUiError.NONE,
                emailError = CreatePatientUiError.NONE
            )
        )
    }

    fun removeSuccessObserver() = viewModelScope.launch {
        _uiState.emit(
            value = _uiState.value.copy(isSuccess = false)
        )
    }
}