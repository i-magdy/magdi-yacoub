package org.myf.ahc.feature.registration.ui.reports

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.myf.ahc.core.model.storage.DocumentModel
import org.myf.ahc.feature.registration.util.FileTypesUtil.subStringFileName
import javax.inject.Inject

class FileStorageRepo @Inject constructor(

) {

    private val storage = Firebase.storage
    private val storageRef = storage.reference
    private val user = Firebase.auth.currentUser
    private val coroutine = CoroutineScope(Dispatchers.Default)
    val isSucceed = MutableStateFlow(false)
    val progress = MutableStateFlow(0)
    val isDeleted = MutableStateFlow(false)
    val document = MutableStateFlow<DocumentModel?>(null)

    fun uploadFile(
        path: String,
        data: ByteArray,
        name: String
    ){
        if (user == null) return
        val ref = storageRef.child("$path/${user.uid}/$name")
        val task = ref.putBytes(data)
        task.addOnCompleteListener { snapshot ->
            coroutine.launch {
                isSucceed.emit(snapshot.isSuccessful)
            }
        }
        task.addOnProgressListener { result ->
            val p:Double = (100.0 * result.bytesTransferred) / result.totalByteCount
            coroutine.launch {
                progress.emit(p.toInt())
                if (p.toInt() == 100){
                    progress.emit(0)
                }
            }
        }
    }

    fun getFileByPath(
        path: String
    ){
        val ref = storageRef.child(path)
        ref.metadata.addOnSuccessListener { meta ->
            val doc = DocumentModel(
                path = meta.path,
                type = meta.contentType ?: "",
                name = subStringFileName(
                    name = meta.name ?: "",
                    type = meta.contentType ?: ""
                ),
                note = meta.getCustomMetadata("note"),
                size = meta.sizeBytes
            )
            coroutine.launch { document.emit(doc) }
        }
    }

    fun updateDocumentNote(
        path: String,
        note: String
    ){
        val ref = storageRef.child(path)
        val meta = storageMetadata {
            setCustomMetadata("note",note)
        }
        ref.updateMetadata(meta).addOnSuccessListener {
            coroutine.launch {
                isSucceed.emit(true)
            }
        }.addOnFailureListener { Log.e("update_note",it.message.toString()) }
    }

    fun deleteFile(
        path: String
    ) = coroutine.launch {
        val ref = storageRef.child(path)
        ref.delete().addOnCompleteListener {
            coroutine.launch {  isDeleted.emit(it.isSuccessful) }
        }.addOnFailureListener { Log.e("delete",it.message.toString()) }
    }

    fun reset() = coroutine.launch {
        isSucceed.emit(false)
        isDeleted.emit(false)
    }

    fun cancelJob() = coroutine.cancel()
}