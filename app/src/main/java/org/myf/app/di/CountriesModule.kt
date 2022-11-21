package org.myf.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import org.myf.app.api.CountriesApiClient
import org.myf.app.api.CountriesService

@Module
@InstallIn(ViewModelComponent::class)
class CountriesModule {

    @Provides
    @ViewModelScoped
    fun getCountriesApiService(): CountriesService = CountriesApiClient.create()

}