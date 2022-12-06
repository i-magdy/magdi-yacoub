package org.myf.ahc.repos

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import javax.inject.Inject

class StorageUploadRepo @Inject constructor(

) {

    private val storage = Firebase.storage
    private val storageRef = storage.reference
    private val user = Firebase.auth.currentUser

    fun uploadFile(
        data: ByteArray,
        name: String
    ){
        if (user == null) return
        val ref = storageRef.child("Patient_reports/${user.uid}/$name")
        val task = ref.putBytes(data)
        task.addOnCompleteListener { snapshot ->
            Log.e("upload","${snapshot.isSuccessful}")
        }.addOnFailureListener { Log.e("upload",it.message.toString()) }
    }

}