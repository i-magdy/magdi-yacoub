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
    private val ioDispatcher: CoroutineDispatcher
): ReadStorageRepository {

    private val storage = Firebase.storage
    private val coroutine = CoroutineScope(ioDispatcher + SupervisorJob())

    override fun getDocuments(
        patientId: String
    ): Flow<Documents> = callbackFlow {
        val listRef = storage.reference.child("Patient_Reports/$patientId") //TODO
        val list = ArrayList<DocumentModel>()
        val task = listRef.listAll()
        var size = 0L

        coroutine.launch {
            task.addOnSuccessListener {  }
                .asDeferred()
                .await()
                .items.forEach{ storageReference ->
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
        awaitClose {  }
    }


    override fun getDocument(
        path: String
    ): Flow<DocumentModel?> = callbackFlow {
        if (path.isEmpty()) return@callbackFlow
        val ref = storage.reference.child(path)
        coroutine.launch {
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

    override fun cancelJob() = coroutine.cancel()
}