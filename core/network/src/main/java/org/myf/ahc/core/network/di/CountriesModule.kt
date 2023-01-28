package org.myf.ahc.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import org.myf.ahc.core.network.countriesRetrofit.CountriesApiClient
import org.myf.ahc.core.network.countriesRetrofit.CountriesService

@Module
@InstallIn(ViewModelComponent::class)
object CountriesModule {

    @Provides
    @ViewModelScoped
    fun provideCountriesApiService(): CountriesService = CountriesApiClient.create()

}