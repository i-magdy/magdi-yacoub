package org.myf.ahc.di

import android.content.Context
import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.myf.ahc.core.datastore.DatastoreImpl
import org.myf.ahc.core.datastore.PatientData
import org.myf.ahc.core.datastore.PatientDatastore.patientDatastore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesDatastore(@ApplicationContext context: Context): DatastoreImpl = DatastoreImpl.create(context)

    @Provides
    @Singleton
    fun provideProtoDatastore(@ApplicationContext context: Context): DataStore<PatientData> = context.patientDatastore

}