package org.myf.demo.core.network.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.myf.demo.core.common.annotation.DocumentsReference
import org.myf.demo.core.common.annotation.PatientDatabaseRef
import org.myf.demo.core.common.annotation.StorageRef
import org.myf.demo.core.network.BuildConfig


@Module
@InstallIn(SingletonComponent::class)
object StorageClientModule {

    @Provides
    @DocumentsReference
    fun providesFirebaseStorageDocumentsReference(): StorageReference = Firebase.storage.reference.child(BuildConfig.DOCUMENTS_PATH)

    @Provides
    @StorageRef
    fun providesFirebaseStorageReference(): StorageReference = Firebase.storage.reference

    @Provides
    @PatientDatabaseRef
    fun providesPatientFireStoreReference(): CollectionReference = Firebase.firestore.collection(BuildConfig.PATIENT_DB_REF)

}