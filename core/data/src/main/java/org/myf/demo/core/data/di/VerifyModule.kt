package org.myf.demo.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import org.myf.demo.core.common.annotation.Google
import org.myf.demo.core.data.repository.*

@Module
@InstallIn(ViewModelComponent::class)
interface VerifyModule {

    @Binds
    fun bindsCountriesRepository(
        countriesRepo: CountriesRepositoryImpl
    ): CountriesRepository

    @Google
    @Binds
    fun bindsVerificationService(
        verificationService: VerificationServiceRepositoryImpl
    ): VerificationServiceRepository

    @Binds
    fun bindsVerificationRepository(
        verificationRepo: VerificationRepositoryImpl
    ): VerificationRepository

}