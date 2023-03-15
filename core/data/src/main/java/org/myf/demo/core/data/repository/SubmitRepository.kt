package org.myf.demo.core.data.repository

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.MutableStateFlow
import org.myf.demo.core.datastore.PatientModel

interface SubmitRepository {

    val patient: MutableStateFlow<PatientModel>

    suspend fun createPatientAsync(): Deferred<Boolean>
    fun cancelJob()
}