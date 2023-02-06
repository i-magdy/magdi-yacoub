package org.myf.demo.core.network.countriesRetrofit

import org.myf.demo.core.network.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CountriesApiClient {
    fun create(): CountriesService =
        Retrofit.Builder()
            .baseUrl(BuildConfig.COUNTRIES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountriesService::class.java)
}