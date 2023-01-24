package org.myf.ahc.core.data.repository

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.myf.ahc.core.common.util.FileTypesUtil.subStringFileName
import org.myf.ahc.core.model.storage.DocumentModel

import javax.inject.Inject

@Deprecated("use [ReadDocumentsRepository]")
class StorageListRepo @Inject constructor(){

    private val storage = Firebase.storage
    private val coroutine = CoroutineScope(Dispatchers.Default)
    val size = MutableStateFlow(0L)
    val files = MutableStateFlow<List<DocumentModel>>(emptyList())

    fun getPatientReportsList(
        patientId: String
    ){
        val listRef = storage.reference.child("Patient_Reports/$patientId")//TODO
        val list = ArrayList<DocumentModel>()
        var s = 0L

        coroutine.launch {
            listRef.listAll().addOnSuccessListener {  result ->
                result.items.forEach { storageReference ->
                    storageReference.metadata.addOnSuccessListener { meta ->
                        list.add(
                            DocumentModel(
                                path = meta.path,
                                type = meta.contentType ?: "",
                                name = subStringFileName(
                                    name = meta.name ?: "",
                                    type = meta.contentType ?: ""
                                ),
                                note = meta.getCustomMetadata("note"),
                                size = meta.sizeBytes,
                                url = ""
                            )
                        )
                        s += meta.sizeBytes
                        if (result.items.size == list.size){
                            coroutine.launch {
                                size.emit(
                                    value = s
                                )
                                files.emit(list)
                            }
                        }
                    }
                }
            }
        }
    }

    fun cancelJob() = coroutine.cancel()
}