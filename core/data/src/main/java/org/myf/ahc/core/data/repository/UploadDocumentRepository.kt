package org.myf.ahc.core.data.repository

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.myf.ahc.core.model.storage.UploadDocumentModel
import javax.inject.Inject

class UploadDocumentRepository @Inject constructor(
    ioDispatcher: CoroutineDispatcher
) : StorageRepository {

    private val coroutine: CoroutineScope = CoroutineScope(ioDispatcher + SupervisorJob())
    private val storageRef = Firebase.storage.reference
    private val user = Firebase.auth.currentUser

    override fun uploadDocument(
        path: String,
        data: ByteArray,
        name: String
    ): Flow<UploadDocumentModel> = callbackFlow {
        if (user == null){ send(UploadDocumentModel()) }
        val ref = storageRef.child("$path/${user?.uid}/$name")
        val task = ref.putBytes(data)
        task.addOnCompleteListener { snapshot ->
            if (snapshot.isSuccessful){
                coroutine.launch {
                    try {
                        send(
                            UploadDocumentModel(
                                isUploaded = true,
                                progress = 0
                            )
                        )
                    } catch (t: Throwable) {
                        close()
                    }
                }
            }
        }.addOnProgressListener { result ->
            var p:Double = (100.0 * result.bytesTransferred) / result.totalByteCount
            coroutine.launch {
                if (p.toInt() == 100){ p = 0.0 }
                try {
                    send(UploadDocumentModel(
                        progress = p.toInt()
                    ))
                }catch (t: Throwable){
                   close()
                }
            }
        }.addOnFailureListener {
            coroutine.launch {
                try {
                    send(UploadDocumentModel())
                }catch (t: Throwable){
                    close()
                }
            }
            it.printStackTrace()
        }
        awaitClose { task.removeOnProgressListener { task.removeOnCompleteListener {  } } }
    }

    override fun addDocumentNote(
        path: String,
        note: String
    ): Flow<Boolean> = callbackFlow {
        val ref = storageRef.child(path)
        val meta = storageMetadata {
            setCustomMetadata("note",note)
        }
        val task = ref.updateMetadata(meta)
        task.addOnSuccessListener {
            coroutine.launch {
                try {
                    send(true)
                }catch (t: Throwable){
                    close(t)
                }
            }
        }.addOnFailureListener {
            coroutine.launch {
                try {
                    send(true)
                }catch (t: Throwable){
                    close(t)
                }
            }
            Log.e("update_document_note",it.message.toString())
        }
        awaitClose {  }
    }

    override fun deleteDocument(
        path: String
    ): Flow<Boolean> = callbackFlow {
        val ref = storageRef.child(path)
        val task = ref.delete()
        task.addOnCompleteListener {
            coroutine.launch {
                try {
                    send(it.isSuccessful)
                }catch (t: Throwable){
                    close(t)
                }
            }
        }.addOnFailureListener {
            coroutine.launch {
                try {
                    send(false)
                }catch (t: Throwable){
                    close(t)
                }
            }
            Log.e("delete_document",it.message.toString())
        }
        awaitClose {  }
    }

    override fun cancelJob() = coroutine.cancel()

}