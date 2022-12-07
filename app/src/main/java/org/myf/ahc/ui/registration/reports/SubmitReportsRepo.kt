package org.myf.ahc.ui.registration.reports

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import org.myf.ahc.repos.StorageListRepo
import org.myf.ahc.repos.FileStorageRepo
import org.myf.ahc.util.EndPoints
import javax.inject.Inject

class SubmitReportsRepo @Inject constructor(
    private val storageRepo: FileStorageRepo,
    private val storageListRepo: StorageListRepo
) {

    private val coroutine = CoroutineScope(Dispatchers.IO)
    private val listenerCoroutine = CoroutineScope(Dispatchers.IO)
    val uiState = MutableStateFlow(ReportsUiState())
    private val user = Firebase.auth.currentUser

    init {
        listenerCoroutine.launch {
            launch {
                storageListRepo.files.collect {
                    uiState.emit(
                        uiState.value.copy(
                            list = it,
                            isLoading = false,
                            isEmpty = it.isEmpty()
                        )
                    )
                }
            }
            launch {
                storageListRepo.size.collect {
                    uiState.emit(uiState.value.copy(size = it))
                }
            }
            launch {
                storageRepo.isSucceed.collect {
                    if (it) {
                        getReportsList()
                        storageRepo.reset()
                    }
                }
            }
            launch {
                storageRepo.progress.collect { progress ->
                    uiState.emit(uiState.value.copy(progress = progress))
                }
            }
            launch {
                storageRepo.isDeleted.collect {
                    if (it) {
                        clearDeleteFile()
                        getReportsList()
                    }
                }
            }
        }
    }

    fun uploadFile(
        data: ByteArray,
        name: String
    ) = coroutine.launch {
        storageRepo.uploadFile(
            path = EndPoints.REPORTS_PATH,
            data = data,
            name = name
        )
        uiState.emit(uiState.value.copy(fileName = name))
    }

    fun getReportsList() = coroutine.launch {
        user ?: return@launch
        storageListRepo.getPatientReportsList(user.uid)
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
        storageRepo.reset()
    }

    fun deleteFile() = coroutine.launch {
        val path = uiState.value.deleteFile?.path ?: ""
        if (path.isNotEmpty()){
            storageRepo.deleteFile(
                path = path
            )
        }else{
            clearDeleteFile()
        }
    }


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
        storageRepo.cancelJob()
        storageListRepo.cancelJob()
    }
}