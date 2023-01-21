package org.myf.ahc.feature.registration.ui.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.myf.ahc.core.common.util.FileTypesUtil
import org.myf.ahc.core.data.repository.UploadReportsRepository
import org.myf.ahc.core.model.storage.DocumentModel
import org.myf.ahc.core.model.uiState.ReportsUiState
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repo: UploadReportsRepository
) : ViewModel() {

    val uiState: StateFlow<ReportsUiState> = repo.uiState
    val editDocument: StateFlow<DocumentModel?> = repo.editDocument

    fun getReportsList() = repo.getReportsList()

    fun getReportByPath(
        path: String
    ) = repo.getReportByPath(
        path = path
    )

    fun uploadFile(
        data: ByteArray,
        name: String
    ) = repo.uploadFile(
        data = data,
        name = name
    )

    fun updateDocumentNote(
        path: String,
        note: String
    ) = repo.updateDocumentNote(
        path = path,
        note = note
    )

    fun onAttemptToDeleteFile(
        path: String
    ) = repo.attemptToDeleteFile(
        path = path
    )

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

    fun clearDeleteFile() = repo.clearDeleteFile()

    fun deleteFile() = repo.deleteFile()

    fun openFiles() = repo.openFiles()

    fun openImage() = repo.openImage()

    fun clearOpenFiles() = repo.clearOpenFiles()

    override fun onCleared() {
        super.onCleared()
        repo.cancelJob()
    }
}