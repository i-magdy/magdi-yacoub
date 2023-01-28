package org.myf.ahc.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.myf.ahc.core.common.annotation.Google
import org.myf.ahc.core.data.repository.FirebaseDocumentsRepository
import org.myf.ahc.core.data.repository.ReadStorageRepository
import org.myf.ahc.core.data.repository.StorageRepository
import org.myf.ahc.core.data.repository.FirebaseUploadDocumentRepository

@Module
@InstallIn(SingletonComponent::class)
interface StorageModule {

    @Google
    @Binds
    fun bindsStorageRepository(
        storageRepo: FirebaseUploadDocumentRepository
    ): StorageRepository

    @Google
    @Binds
    fun bindsDocumentsRepository(
         readStorageRepo: FirebaseDocumentsRepository
    ): ReadStorageRepository

}