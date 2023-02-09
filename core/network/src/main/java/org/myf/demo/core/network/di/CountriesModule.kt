package org.myf.demo.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import org.myf.demo.core.network.countriesRetrofit.CountriesApiClient
import org.myf.demo.core.network.countriesRetrofit.CountriesService

@Module
@InstallIn(SingletonComponent::class)
object CountriesModule {

    @Provides
    fun provideCountriesApiService(): CountriesService = CountriesApiClient.create()

}