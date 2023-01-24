package org.myf.ahc.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.myf.ahc.core.model.storage.DocumentModel
import org.myf.ahc.core.model.storage.Documents

interface ReadStorageRepository {

    fun getDocuments(patientId: String): Flow<Documents>

    fun getDocument(path: String) : Flow<DocumentModel?>

    fun cancelJob()
}