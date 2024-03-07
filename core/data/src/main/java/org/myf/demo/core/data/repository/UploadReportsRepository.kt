package org.myf.demo.core.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import org.myf.demo.core.common.uiState.ReportsUiState
import org.myf.demo.core.model.storage.DocumentModel

interface UploadReportsRepository {

    val uiState: MutableStateFlow<ReportsUiState>
    val editDocument:  MutableStateFlow<DocumentModel?>

    suspend fun uploadFile(data: ByteArray, name: String)
    suspend fun getDocumentsList()
    suspend fun getDocumentByPath(path: String)
    suspend fun updateDocumentNote(path: String, note: String)
    suspend fun attemptToDeleteFile(path: String)
    suspend fun clearDeleteFile()
    suspend fun deleteFile()
    suspend fun saveFilesCount()
    suspend fun openFiles()
    suspend fun openImage()
    suspend fun confirmDeleteFile()
    suspend fun clearOpenFiles()
    fun cancelJob()
}