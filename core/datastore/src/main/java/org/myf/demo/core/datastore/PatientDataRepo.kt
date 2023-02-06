package org.myf.demo.core.datastore

import android.net.Uri
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.myf.demo.core.model.storage.DocumentModel
import javax.inject.Inject


class PatientDataRepo @Inject constructor(
    private val dataStore: DataStore<PatientData>
) {

    suspend fun getPatientMessage(): Flow<PatientModel> = flow{
        patient.collect{
            emit(
                PatientModel(
                    name = it.name,
                    id = it.id,
                    email = it.email,
                    img = Uri.parse(it.imgUri),
                    primaryPhone = it.primaryPhone,
                    secondaryPhone = it.secondaryPhone,
                    fileCount = it.documentsMap.size,
                    isVerified = it.verified,
                    deviceToken = it.deviceToken
                )
            )
        }
    }

    private val patient: Flow<PatientData> = dataStore.data.catch {
        if (it is IOException) {
            Log.e("patient_datastore", "Error reading patient data.", it)
            emit(PatientData.getDefaultInstance())
        } else {
            throw it
        }
    }

    suspend fun updatePatientDataOnCreateScreen(
        name: String,
        id: String,
        img: Uri,
        email: String /* Optional */
    ) {
        dataStore.updateData { patient ->
            patient.toBuilder()
                .setName(name)
                .setId(id)
                .setEmail(email)
                .setImgUri(img.toString())
                .build()
        }
    }


    suspend fun updatePatientDataOnVerifyScreen(
        primaryPhone: String,
        verified: Boolean
    ) {
        dataStore.updateData { patient ->
            patient.toBuilder()
                .setPrimaryPhone(primaryPhone)
                .setVerified(verified)
                .build()
        }
    }

    suspend fun updateSecondaryPhone(
        secondaryPhone: String
    ){
        dataStore.updateData { patient ->
            patient.toBuilder()
                .setSecondaryPhone(secondaryPhone)
                .build()
        }
    }

    suspend fun updatePatientDataOnReportsScreen(
        documents: List<DocumentModel>
    ) {
        dataStore.updateData { data ->
            data.toBuilder().clearDocuments().build()
        }
        val documentMap: MutableMap<String,Document> = mutableMapOf()
        documents.forEach { document ->
            val documentBuilder = Document.newBuilder()
            documentBuilder.name = document.name
            documentBuilder.type = document.type
            documentBuilder.url = document.url
            documentBuilder.size = document.size.toInt()
            documentBuilder.note = document.note ?: ""
            documentMap[document.url] = documentBuilder.build()
        }
        dataStore.updateData { patient ->
            patient.toBuilder()
                .putAllDocuments(documentMap)
                .build()
        }
    }
}