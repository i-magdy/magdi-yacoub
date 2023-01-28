package org.myf.ahc.core.network.di

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.myf.ahc.core.common.annotation.DocumentsReference
import org.myf.ahc.core.common.annotation.StorageRef
import org.myf.ahc.core.network.BuildConfig


@Module
@InstallIn(SingletonComponent::class)
object StorageClientModule {

    @Provides
    @DocumentsReference
    fun providesFirebaseStorageDocumentsReference(): StorageReference = Firebase.storage.reference.child(BuildConfig.DOCUMENTS_PATH)

    @Provides
    @StorageRef
    fun providesFirebaseStorageReference(): StorageReference = Firebase.storage.reference

}