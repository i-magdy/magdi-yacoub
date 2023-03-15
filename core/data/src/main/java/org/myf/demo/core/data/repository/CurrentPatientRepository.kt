package org.myf.demo.core.data.repository

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.MutableStateFlow
import org.myf.demo.core.common.model.CurrentPatientModel

interface CurrentPatientRepository {
    val patient: MutableStateFlow<CurrentPatientModel>

    suspend fun getPatientAsync(): Deferred<Boolean>
    suspend fun getPatientAsync(id: String): Deferred<Boolean>
    fun cancelJob()
}