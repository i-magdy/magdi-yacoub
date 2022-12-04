package org.myf.ahc.ui.registration.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(

): ViewModel(){

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState>  = _uiState


    fun openFiles() = viewModelScope.launch {
        _uiState.emit(
            value = _uiState.value.copy(pickFile = true)
        )
    }

    fun openImage() = viewModelScope.launch {
        _uiState.emit(
            value = _uiState.value.copy(pickImage = true)
        )
    }

    fun clearOpenFiles() = viewModelScope.launch {
        _uiState.emit(
            value = _uiState.value.copy(pickImage = false, pickFile = false)
        )
    }
}