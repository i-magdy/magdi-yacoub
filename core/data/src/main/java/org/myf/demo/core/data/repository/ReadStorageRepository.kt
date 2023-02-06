package org.myf.demo.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.myf.demo.core.model.storage.DocumentModel
import org.myf.demo.core.model.storage.Documents

interface ReadStorageRepository {

    fun getDocuments(patientId: String): Flow<Documents>

    fun getDocument(path: String) : Flow<DocumentModel?>

    fun cancelJob()
}