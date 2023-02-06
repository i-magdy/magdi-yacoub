package org.myf.demo.core.data.repository

import android.util.Log
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.asDeferred
import org.myf.demo.core.common.annotation.Dispatcher
import org.myf.demo.core.common.annotation.DocumentsReference
import org.myf.demo.core.common.annotation.MyDispatchers
import org.myf.demo.core.common.annotation.StorageRef
import org.myf.demo.core.common.util.FileTypesUtil
import org.myf.demo.core.model.storage.DocumentModel
import org.myf.demo.core.model.storage.Documents
import javax.inject.Inject

class FirebaseDocumentsRepository @Inject constructor(
    @Dispatcher(MyDispatchers.IO) ioDispatcher: CoroutineDispatcher,
    @DocumentsReference private val documentsReference: StorageReference,
    @StorageRef private val reference: StorageReference
): ReadStorageRepository {

    private val coroutine = CoroutineScope(ioDispatcher + SupervisorJob())
    private var fetchDocumentsJob: Job? = null
    private var fetchDocumentJob: Job? = null


    override fun getDocuments(
        patientId: String
    ): Flow<Documents> = callbackFlow {
        val listRef = documentsReference.child(patientId)
        val list = ArrayList<DocumentModel>()
        val task = listRef.listAll()
        var size = 0L
        fetchDocumentsJob?.cancel()
        fetchDocumentsJob = coroutine.launch {
            val result =task.addOnSuccessListener {  }
                .asDeferred().await()

            result.items.ifEmpty {
                trySend(Documents())
                    .onFailure {
                        close(it)
                        Log.e("documents",it?.message.toString())
                    }
                channel.close()
            }

            result.items.forEach{ storageReference ->
                    val meta = getDocumentMetadata(storageReference)
                    val uri = getDocumentUrl(storageReference)
                    list.add(
                        DocumentModel(
                            path = meta.path,
                            type = meta.contentType ?: "",
                            name = FileTypesUtil.subStringFileName(
                                name = meta.name ?: "",
                                type = meta.contentType ?: ""
                            ),
                            note = meta.getCustomMetadata("note"),
                            size = meta.sizeBytes,
                            url = uri.toString()
                        )
                    )
                    size += meta.sizeBytes
                    val documents = Documents(
                        documents = list,
                        totalSize = size
                    )
                    trySendBlocking(documents)
                        .onFailure {
                            close(it)
                            Log.e("documents",it?.message.toString())
                        }
                }
            channel.close()
        }
        task.addOnFailureListener {
            trySend(Documents())
                .onFailure {t ->
                    close(t)
                }
            Log.e("documents",it.message.toString())
        }
        awaitClose { Log.e("docs","canceled") }
    }


    override fun getDocument(
        path: String
    ): Flow<DocumentModel?> = callbackFlow {
        if (path.isEmpty()) return@callbackFlow
        val ref = reference.child(path)
        fetchDocumentJob?.cancel()
        fetchDocumentJob = coroutine.launch {
            val meta = getDocumentMetadata(ref)
            val uri = getDocumentUrl(ref)
            val doc = DocumentModel(
                path = meta.path,
                type = meta.contentType ?: "",
                name = FileTypesUtil.subStringFileName(
                    name = meta.name ?: "",
                    type = meta.contentType ?: ""
                ),
                note = meta.getCustomMetadata("note"),
                size = meta.sizeBytes,
                url = uri.toString()
            )
            trySendBlocking(doc)
                .onFailure {
                    close(it)
                    it?.printStackTrace()
                }
            channel.close()
        }
        awaitClose {  }
    }

    private suspend fun getDocumentMetadata(
        ref: StorageReference
    ) = ref.metadata.addOnSuccessListener{ }.asDeferred().await()

    private suspend fun getDocumentUrl(
        ref: StorageReference
    ) = ref.downloadUrl.addOnSuccessListener {  }.asDeferred().await()

    override fun cancelJob() = coroutine.coroutineContext.cancelChildren()

}