package org.myf.ahc.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.myf.ahc.core.datastore.PatientDatastore.patientDatastore
import javax.inject.Singleton
import org.myf.ahc.core.datastore.PatientData


@Module
@InstallIn(SingletonComponent::class)
class DatastoreModule {

   /* @Provides
    @Singleton
    fun provideDatastoreImpl(@ApplicationContext context: Context) = DatastoreImpl(context)*/

   /* @Provides
    @Singleton
    fun provideProtoDatastore(@ApplicationContext context: Context): DataStore<PatientData> = context.patientDatastore*/

}