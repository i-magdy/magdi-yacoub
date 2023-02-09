package org.myf.demo.core.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.myf.demo.core.common.annotation.Dispatcher
import org.myf.demo.core.common.annotation.Google
import org.myf.demo.core.common.annotation.MyDispatchers.IO
import org.myf.demo.core.common.uiState.ReportsUiState
import org.myf.demo.core.datastore.PatientDataRepo
import org.myf.demo.core.model.storage.DocumentModel
import javax.inject.Inject

class UploadReportsRepositoryImpl @Inject constructor(
    @Google private val readStorageRepo: ReadStorageRepository,
    @Google private val modifyDocumentRepo: StorageRepository,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val patientRepo: PatientDataRepo
): UploadReportsRepository {


    private val coroutine = CoroutineScope(ioDispatcher + SupervisorJob())
    private val user = Firebase.auth.currentUser
    override val uiState: MutableStateFlow<ReportsUiState> = MutableStateFlow(ReportsUiState())
    override val editDocument = MutableStateFlow<DocumentModel?>(null)
    private var fetchJob: Job? = null

    override suspend fun uploadFile(
        data: ByteArray,
        name: String
    ) {
        modifyDocumentRepo.uploadDocument(
            data = data,
            name = name
        ).collect {
            if (it.isUploaded) {
                getDocumentsList()
            }
            uiState.emit(
                value = uiState.value.copy(
                    fileName = name,
                    isUploading = !it.isUploaded,
                    progress = it.progress,
                    isLoading = it.progress == 0
                )
            )
        }
    }

    override suspend fun getDocumentsList() {
        fetchJob?.cancel()
        fetchJob = coroutine.launch{
            user ?: return@launch
            readStorageRepo.getDocuments(user.uid).collect { docs ->
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
    }

    override suspend fun getDocumentByPath(
        path: String
    ) {
        readStorageRepo.getDocument(
            path = path
        ).collect { editDocument.emit(it) }
    }

    override suspend fun updateDocumentNote(
        path: String,
        note: String
    ){
        modifyDocumentRepo.addDocumentNote(
            path = path,
            note = note
        ).collect{
            if (it) { getDocumentsList() }
        }
    }

    override suspend fun attemptToDeleteFile(
        path: String
    ) {
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

    override suspend fun clearDeleteFile() {
        uiState.emit(
            value = uiState.value.copy(
                deleteFile = null
            )
        )
    }

    override suspend fun deleteFile() {
        val path = uiState.value.deleteFile?.path ?: ""
        if (path.isNotEmpty()){
            modifyDocumentRepo.deleteDocument(
                path = path
            ).collect{
                if (it) {
                    clearDeleteFile()
                    getDocumentsList()
                }
            }
        }else{
            clearDeleteFile()
        }
    }

    override suspend fun saveFilesCount() = patientRepo.updatePatientDataOnReportsScreen(
        documents = uiState.value.list
    )


    override suspend fun openFiles() {
        uiState.emit(
            value = uiState.value.copy(pickFile = true)
        )
    }

    override suspend fun openImage() {
        uiState.emit(
            value = uiState.value.copy(pickImage = true)
        )
    }

    override suspend fun clearOpenFiles() {
        uiState.emit(
            value = uiState.value.copy(pickImage = false, pickFile = false)
        )
    }

    override fun cancelJob() {
        coroutine.coroutineContext.cancelChildren()
        modifyDocumentRepo.cancelJob()
        readStorageRepo.cancelJob()
    }

}