package org.myf.ahc.ui.registration.reports

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.myf.ahc.repos.StorageListRepo
import org.myf.ahc.repos.StorageUploadRepo
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val uploadRepo: StorageUploadRepo,
    private val storageListRepo: StorageListRepo
): ViewModel(){

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState>  = _uiState

    init {
        viewModelScope.launch {
            launch { storageListRepo.files.collect{
                _uiState.emit(
                    _uiState.value.copy(
                        list = it,
                        isLoading = it.isEmpty(),
                        isEmpty = it.isEmpty()
                    )
                )

            } }
            launch { storageListRepo.size.collect{
                _uiState.emit(_uiState.value.copy(size = it))
            } }
            launch {
                uploadRepo.isSucceed.collect{
                    if (it){
                        getReportsList()
                        uploadRepo.reset()
                    }
                }
            }
            launch {
                uploadRepo.progress.collect{ progress ->
                    _uiState.emit(_uiState.value.copy(progress = progress))
                }
            }
        }
    }

    fun getReportsList() = viewModelScope.launch {
        val user = Firebase.auth.currentUser ?: return@launch
        storageListRepo.getPatientReportsList(user.uid)
    }

    fun uploadFile(
        data: ByteArray,
        name: String
    ) = viewModelScope.launch {
       uploadRepo.uploadFile(
           data = data,
           name = name
       )
        _uiState.emit(_uiState.value.copy(fileName = name))
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

    override fun onCleared() {
        super.onCleared()
        uploadRepo.cancelJob()
        storageListRepo.cancelJob()
    }
}