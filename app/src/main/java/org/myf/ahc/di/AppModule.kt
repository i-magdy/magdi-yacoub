package org.myf.ahc.di

import android.content.Context
import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.myf.ahc.PatientData
import org.myf.ahc.data.DatastoreImpl
import org.myf.ahc.data.PatientDatastore.patientDatastore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDatastoreImpl(@ApplicationContext context: Context) = DatastoreImpl(context)

    @Provides
    @Singleton
    fun provideProtoDatastore(@ApplicationContext context: Context): DataStore<PatientData> = context.patientDatastore

}