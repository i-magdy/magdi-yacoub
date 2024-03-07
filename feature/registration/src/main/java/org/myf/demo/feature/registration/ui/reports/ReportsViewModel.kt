package org.myf.demo.feature.registration.ui.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.myf.demo.core.common.uiState.ReportsUiState
import org.myf.demo.core.common.util.FileTypesUtil
import org.myf.demo.core.data.repository.UploadReportsRepository
import org.myf.demo.core.model.storage.DocumentModel
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repo: UploadReportsRepository
) : ViewModel() {

    val uiState: StateFlow<ReportsUiState> = repo.uiState
    val editDocument: StateFlow<DocumentModel?> = repo.editDocument

    fun getReportsList() = viewModelScope.launch { repo.getDocumentsList() }

    fun getDocumentByPath(
        path: String
    ) = viewModelScope.launch {
        repo.getDocumentByPath(
            path = path
        )
    }

    fun uploadFile(
        data: ByteArray,
        name: String
    ) = viewModelScope.launch {
        repo.uploadFile(
            data = data,
            name = name
        )
    }

    fun updateDocumentNote(
        path: String,
        note: String
    ) = viewModelScope.launch {
        repo.updateDocumentNote(
            path = path,
            note = note
        )
    }

    fun onAttemptToDeleteFile(
        path: String
    ) = viewModelScope.launch {
        repo.attemptToDeleteFile(
            path = path
        )
    }

    suspend fun isFileExist(
        name: String
    ): Boolean = withContext(Dispatchers.Default) {
        uiState.value.list.forEach {
            val file = it.name + FileTypesUtil.getFileTypeExtension(it.type)
            if (name == file) {
                return@withContext true
            }
        }
        false
    }

    fun saveFilesCount() = viewModelScope.launch {
        repo.saveFilesCount()
    }

    fun removeDeleteFileObserver() = viewModelScope.launch {
        repo.clearDeleteFile()
    }

    fun deleteFile() = viewModelScope.launch {
        repo.deleteFile()
    }

    fun openFiles() = viewModelScope.launch {
        repo.openFiles()
    }

    fun openImage() = viewModelScope.launch { repo.openImage() }

    fun confirmDeleteFile() = viewModelScope.launch { repo.confirmDeleteFile() }

    fun removeOpenFilesObserver() = viewModelScope.launch { repo.clearOpenFiles() }

    override fun onCleared() {
        super.onCleared()
        repo.cancelJob()
    }
}