package org.myf.ahc.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.myf.ahc.core.model.storage.UploadDocumentModel

interface StorageRepository {

    fun uploadDocument(
        path: String,
        data: ByteArray,
        name: String
    ): Flow<UploadDocumentModel>

    fun addDocumentNote(
        path: String,
        note: String
    ):Flow<Boolean>

    fun deleteDocument(
        path: String
    ): Flow<Boolean>

    fun cancelJob()
}