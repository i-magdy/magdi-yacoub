package org.myf.demo.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.myf.demo.core.model.storage.UploadDocumentModel

interface StorageRepository {

    fun uploadDocument(
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