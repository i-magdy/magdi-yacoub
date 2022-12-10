package org.myf.ahc.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import org.myf.ahc.api.CountriesApiClient
import org.myf.ahc.api.CountriesService

@Module
@InstallIn(ViewModelComponent::class)
class CountriesModule {

    @Provides
    @ViewModelScoped
    fun provideCountriesApiService(): CountriesService = CountriesApiClient.create()

}