package org.myf.ahc.core.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.myf.ahc.core.data.util.CountriesUtil
import org.myf.ahc.core.model.countries.CountryCodeModel
import org.myf.ahc.core.model.countries.CountryModel
import org.myf.ahc.core.network.countriesRetrofit.CountriesService
import retrofit2.Response
import javax.inject.Inject

class CountriesRepository @Inject constructor(
    private val countriesService: CountriesService
) {

    suspend fun getCountryByCode(
        code: String
    ): CountryCodeModel? = withContext(Dispatchers.IO){
        val response: Response<List<CountryModel>> = countriesService.getCountryByCode(code)
        val body = response.body()
        if (response.isSuccessful && !body.isNullOrEmpty()) {
            CountryCodeModel(
                ar_name = body[0].translations["ara"]?.common ?: "",
                en_name = body[0].name.common,
                code = if (null == body[0].idd) "" else body[0].idd?.root +
                        if (body.isEmpty()) "" else
                            body[0].idd?.suffixes?.get(0) ?: "",
                flag = body[0].flag
            )
        }else{null}
    }

    suspend fun getCountries(): List<CountryCodeModel>  = withContext(Dispatchers.IO){
        val response: Response<List<CountryModel>> = countriesService.getAllCountriesData()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            CountriesUtil.getCountries(body)
        }else{
            emptyList()
        }
    }

}