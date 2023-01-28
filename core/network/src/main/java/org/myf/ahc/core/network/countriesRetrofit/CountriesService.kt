package org.myf.ahc.core.network.countriesRetrofit

import org.myf.ahc.core.model.countries.CountryModel
import org.myf.ahc.core.network.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CountriesService {

    @GET(BuildConfig.COUNTRIES)
    suspend fun getAllCountriesData(): Response<List<CountryModel>>

    @GET(BuildConfig.COUNTRY_BY_CODE)
    suspend fun getCountryByCode(
        @Path("code") code: String
    ): Response<List<CountryModel>>

}