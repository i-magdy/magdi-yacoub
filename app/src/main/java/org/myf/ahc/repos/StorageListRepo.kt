package org.myf.ahc.repos

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.myf.ahc.models.DocumentModel
import org.myf.ahc.util.EndPoints
import org.myf.ahc.util.FileTypesUtil
import javax.inject.Inject

class StorageListRepo @Inject constructor(){

    private val storage = Firebase.storage
    private val coroutine = CoroutineScope(Dispatchers.Default)
    val size = MutableStateFlow(0L)
    val files = MutableStateFlow<List<DocumentModel>>(emptyList())

    fun getPatientReportsList(
        patientId: String
    ){
        val listRef = storage.reference.child("${EndPoints.REPORTS_PATH}/$patientId")
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
                                size = meta.sizeBytes
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

    private fun subStringFileName(
        name: String,
        type: String
    ): String{
        return when(type){
            FileTypesUtil.MICROSOFT_WORD -> name.replace(FileTypesUtil.WORD_EX,"")
            FileTypesUtil.JPG -> name.replace(FileTypesUtil.JPG_EX,"")
            FileTypesUtil.PNG -> name.replace(FileTypesUtil.PNG_EX,"")
            FileTypesUtil.PDF -> name.replace(FileTypesUtil.PDF_EX,"")
            else -> ""
        }
    }

    fun cancelJob() = coroutine.cancel()
}