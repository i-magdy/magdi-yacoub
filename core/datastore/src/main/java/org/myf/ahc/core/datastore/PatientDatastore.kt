package org.myf.ahc.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore

object PatientDatastore {
    private const val DATA_STORE_FILE_NAME = "patient_data.pb"
    val Context.patientDatastore: DataStore<PatientData> by dataStore(
        fileName = DATA_STORE_FILE_NAME,
        serializer = PatientSerializer
    )
}