package org.myf.ahc.repos

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class StorageUploadRepo @Inject constructor(

) {

    private val storage = Firebase.storage
    private val storageRef = storage.reference
    private val user = Firebase.auth.currentUser
    private val coroutine = CoroutineScope(Dispatchers.Default)
    val isSucceed = MutableStateFlow(false)
    val progress = MutableStateFlow(0)

    fun uploadFile(
        data: ByteArray,
        name: String
    ){
        if (user == null) return
        val ref = storageRef.child("Patient_reports/${user.uid}/$name")
        val task = ref.putBytes(data)
        task.addOnCompleteListener { snapshot ->
            coroutine.launch {
                isSucceed.emit(snapshot.isSuccessful)
            }
        }.addOnFailureListener { Log.e("upload",it.message.toString()) }
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

    fun reset() = coroutine.launch {
        isSucceed.emit(false)
    }

    fun cancelJob() = coroutine.cancel()
}