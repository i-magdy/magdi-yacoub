package org.myf.demo.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.myf.demo.core.common.annotation.Google
import org.myf.demo.core.data.repository.FirebaseDocumentsRepository
import org.myf.demo.core.data.repository.ReadStorageRepository
import org.myf.demo.core.data.repository.StorageRepository
import org.myf.demo.core.data.repository.FirebaseUploadDocumentRepository

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