package org.myf.demo.core.data.repository

import org.myf.demo.core.model.countries.CountryCodeModel

interface CountriesRepository {

    suspend fun getCountries(): List<CountryCodeModel>

    suspend fun getCountryByCode(
        code: String
    ): CountryCodeModel?

}