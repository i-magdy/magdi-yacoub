package org.myf.ahc.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import org.myf.ahc.core.common.annotation.Dispatcher
import org.myf.ahc.core.common.annotation.MyDispatchers.IO
import org.myf.ahc.core.data.repository.DocumentsRepository
import org.myf.ahc.core.data.repository.ReadStorageRepository
import org.myf.ahc.core.data.repository.StorageRepository
import org.myf.ahc.core.data.repository.UploadDocumentRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun providesStorageRepository(
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher
    ): StorageRepository = UploadDocumentRepository(ioDispatcher)

    @Provides
    @Singleton
    fun providesDocumentsRepository(
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher
    ): ReadStorageRepository = DocumentsRepository(ioDispatcher)

}