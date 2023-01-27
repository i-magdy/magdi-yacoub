package org.myf.ahc.core.data.repository

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.asDeferred
import org.myf.ahc.core.common.util.FileTypesUtil
import org.myf.ahc.core.model.storage.DocumentModel
import org.myf.ahc.core.model.storage.Documents

class FirebaseDocumentsRepository(
    ioDispatcher: CoroutineDispatcher
): ReadStorageRepository {

    private val storage = Firebase.storage
    private val coroutine = CoroutineScope(ioDispatcher + SupervisorJob())
    private var fetchDocumentsJob: Job? = null
    private var fetchDocumentJob: Job? = null


    override fun getDocuments(
        patientId: String
    ): Flow<Documents> = callbackFlow {
        val listRef = storage.reference.child("Patient_Reports/$patientId") //TODO
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
        val ref = storage.reference.child(path)
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