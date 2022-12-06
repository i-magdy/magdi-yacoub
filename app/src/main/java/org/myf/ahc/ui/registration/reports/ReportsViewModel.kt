package org.myf.ahc.ui.registration.reports

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.myf.ahc.repos.StorageUploadRepo
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val uploadRepo: StorageUploadRepo
): ViewModel(){

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState>  = _uiState


    fun uploadFile(
        data: ByteArray,
        name: String
    ) = viewModelScope.launch {
       uploadRepo.uploadFile(
           data = data,
           name = name
       )
    }

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