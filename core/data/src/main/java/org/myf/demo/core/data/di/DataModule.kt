package org.myf.demo.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.myf.demo.core.common.annotation.Google
import org.myf.demo.core.data.repository.*

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

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

    @Binds
    fun bindsUploadReportsRepository(
        reportsRepositoryImpl: UploadReportsRepositoryImpl
    ): UploadReportsRepository

}