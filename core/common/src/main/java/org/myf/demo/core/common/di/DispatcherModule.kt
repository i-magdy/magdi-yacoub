package org.myf.demo.core.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.myf.demo.core.common.annotation.Dispatcher
import org.myf.demo.core.common.annotation.MyDispatchers.IO

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @Provides
    @Dispatcher(IO)
    fun providesIODispatcher():CoroutineDispatcher = Dispatchers.IO
}