package org.myf.ahc.core.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.myf.ahc.core.common.annotation.Dispatcher
import org.myf.ahc.core.common.annotation.MyDispatchers.IO
import org.myf.ahc.core.common.uiState.ReportsUiState
import org.myf.ahc.core.datastore.PatientDataRepo
import org.myf.ahc.core.model.storage.DocumentModel
import javax.inject.Inject

class UploadReportsRepository @Inject constructor(
    private val readStorageRepo: ReadStorageRepository,
    private val patientRepo: PatientDataRepo,
    private val modifyDocumentRepo: StorageRepository,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {


    private val coroutine = CoroutineScope(ioDispatcher + SupervisorJob())
    val uiState = MutableStateFlow(ReportsUiState())
    private val user = Firebase.auth.currentUser
    val editDocument = MutableStateFlow<DocumentModel?>(null)


    suspend fun uploadFile(
        data: ByteArray,
        name: String
    ){
        modifyDocumentRepo.uploadDocument(
            path = "Patient_Reports",
            data = data,
            name = name
        ).collect{
            if (it.isUploaded){ getReportsList() }
            uiState.emit(
                value = uiState.value.copy(
                    fileName = name,
                    isUploading = !it.isUploaded,
                    progress = it.progress
                )
            )
        }
    }

    fun getReportsList() = coroutine.launch {
        user ?: return@launch
        readStorageRepo.getDocuments(user.uid).collect{ docs ->
            uiState.emit(
                uiState.value.copy(
                    list = docs.documents,
                    isLoading = false,
                    isEmpty = docs.documents.isEmpty(),
                    size = docs.totalSize
                )
            )
        }
    }

    fun getReportByPath(
        path: String
    ) = coroutine.launch {
        readStorageRepo.getDocument(
            path = path
        ).collect { editDocument.emit(it) }
    }

    fun updateDocumentNote(
        path: String,
        note: String
    ) = coroutine.launch {
        modifyDocumentRepo.addDocumentNote(
            path = path,
            note = note
        ).collect{
            if (it) { getReportsList() }
        }
    }

    fun attemptToDeleteFile(
        path: String
    ) = coroutine.launch {
        uiState.value.list.forEach {
            if (path == it.path){
                uiState.emit(
                    value = uiState.value.copy(
                        deleteFile = it
                    )
                )
                return@forEach
            }
        }
    }

    fun clearDeleteFile() = coroutine.launch {
        uiState.emit(
            value = uiState.value.copy(
                deleteFile = null
            )
        )
    }

    fun deleteFile() = coroutine.launch {
        val path = uiState.value.deleteFile?.path ?: ""
        if (path.isNotEmpty()){
            modifyDocumentRepo.deleteDocument(
                path = path
            ).collect{
                if (it) {
                    clearDeleteFile()
                    getReportsList()
                }
            }
        }else{
            clearDeleteFile()
        }
    }

    suspend fun saveFilesCount() = patientRepo.updatePatientDataOnReportsScreen(
        fileCount = uiState.value.list.size
    )


    fun openFiles() = coroutine.launch {
        uiState.emit(
            value = uiState.value.copy(pickFile = true)
        )
    }

    fun openImage() = coroutine.launch {
        uiState.emit(
            value = uiState.value.copy(pickImage = true)
        )
    }

    fun clearOpenFiles() = coroutine.launch {
        uiState.emit(
            value = uiState.value.copy(pickImage = false, pickFile = false)
        )
    }


    fun cancelJob() {
        coroutine.cancel()
        modifyDocumentRepo.cancelJob()
        readStorageRepo.cancelJob()
    }
}