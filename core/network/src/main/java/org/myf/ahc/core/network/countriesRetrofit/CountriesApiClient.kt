package org.myf.ahc.core.network.countriesRetrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val REST_COUNTRIES_URL = "https://restcountries.com/"

object CountriesApiClient {
    fun create(): CountriesService =
        Retrofit.Builder()
            .baseUrl(REST_COUNTRIES_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountriesService::class.java)
}