package org.myf.ahc.core.data.repository

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.myf.ahc.core.common.util.FileTypesUtil
import org.myf.ahc.core.model.storage.DocumentModel
import org.myf.ahc.core.model.storage.Documents

class DocumentsRepository(
    private val ioDispatcher: CoroutineDispatcher
): ReadStorageRepository {

    private val storage = Firebase.storage
    private val coroutine = CoroutineScope(ioDispatcher + SupervisorJob())

    override fun getDocuments(
        patientId: String
    ): Flow<Documents> = callbackFlow {
        val listRef = storage.reference.child("Patient_Reports/$patientId")//TODO
        val list = ArrayList<DocumentModel>()
        val task = listRef.listAll()
        var size = 0L
        task.addOnSuccessListener { result ->
            result.items.forEach { storageReference ->
                storageReference.metadata.addOnSuccessListener { meta ->
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
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

                        coroutine.launch {
                            try {
                                send(documents)
                            }catch (t: Throwable){
                                close(t)
                            }
                        }

                    }
                }
            }

        }.addOnFailureListener {
            coroutine.launch {
                try {
                    send(Documents())
                }catch (t: Throwable){
                    close(t)
                }
            }
        }
        awaitClose {  }
    }

    override fun getDocument(
        path: String
    ): Flow<DocumentModel?> = callbackFlow {
        if (path.isEmpty()) return@callbackFlow
        val ref = storage.reference.child(path)
        val task = ref.metadata.addOnSuccessListener { meta ->
            ref.downloadUrl.addOnSuccessListener { uri ->
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
                coroutine.launch {
                    try {
                        send(doc)
                    }catch (t: Throwable){
                        close(t)
                    }
                }
            }.addOnFailureListener {
                coroutine.launch {
                    try {
                        send(null)
                    }catch (t: Throwable){
                        close(t)
                    }
                }
            }
        }.addOnFailureListener {
            coroutine.launch {
                try {
                    send(null)
                }catch (t: Throwable){
                    close(t)
                }
            }
        }
        awaitClose {  }
    }

    override fun cancelJob() = coroutine.cancel()
}