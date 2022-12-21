package org.myf.ahc.repos

import android.net.Uri
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import org.myf.ahc.PatientData
import javax.inject.Inject

class PatientDataRepo @Inject constructor(
    private val dataStore: DataStore<PatientData>
) {

    val patient: Flow<PatientData> = dataStore.data.catch {
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
        secondaryPhone: String
    ) {
        dataStore.updateData { patient ->
            patient.toBuilder()
                .setPrimaryPhone(primaryPhone)
                .setSecondaryPhone(secondaryPhone)
                .build()
        }
    }

    suspend fun updatePatientDataOnReportsScreen(
        verified: Boolean,
        fileCount: Int
    ) {
        dataStore.updateData { patient ->
            patient.toBuilder()
                .setVerified(verified)
                .setFilesCount(fileCount)
                .build()
        }
    }
}