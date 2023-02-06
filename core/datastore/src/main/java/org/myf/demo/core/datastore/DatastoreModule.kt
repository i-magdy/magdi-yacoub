package org.myf.demo.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.myf.demo.core.common.annotation.Dispatcher
import org.myf.demo.core.common.annotation.MyDispatchers.IO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    @Provides
    @Singleton
    fun providesPatientPreferencesDatastore(
        @ApplicationContext context: Context,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher,
        patientSerializer: PatientSerializer
    ): DataStore<PatientData> = DataStoreFactory.create(
        serializer = patientSerializer,
        scope = CoroutineScope(ioDispatcher + SupervisorJob())
    ){
        context.dataStoreFile("patient_data.pb")
    }


    @Provides
    @Singleton
    fun providesDatastore(
        @ApplicationContext context: Context
    ): DatastoreImpl = DatastoreImpl.create(context)

}