package org.myf.demo.core.data.repository

import kotlinx.coroutines.*
import org.myf.demo.core.common.annotation.Dispatcher
import org.myf.demo.core.common.annotation.MyDispatchers.IO
import org.myf.demo.core.data.util.CountriesUtil
import org.myf.demo.core.model.countries.CountryCodeModel
import org.myf.demo.core.network.countriesRetrofit.CountriesService
import javax.inject.Inject

class CountriesRepositoryImpl @Inject constructor(
    private val countriesService: CountriesService,
    @Dispatcher(IO) val ioDispatcher: CoroutineDispatcher
): CountriesRepository {

    override suspend fun getCountryByCode(
        code: String
    ): CountryCodeModel  = withContext(
        context = ioDispatcher + SupervisorJob()
    ){
        try {
            val data = countriesService.getCountryByCode(code).body()!!
            return@withContext data[0].let {
                CountryCodeModel(
                    ar_name = it.translations["ara"]?.common ?: "",
                    en_name = it.name.common,
                    code = if (it.idd == null) "" else it.idd!!.root +
                            if (it.idd!!.suffixes.isNullOrEmpty() || it.idd!!.suffixes?.size!! > 1) "" else
                                it.idd!!.suffixes?.get(0),
                    flag = it.flag
                )
            }
        }catch (e: Exception){
            return@withContext CountryCodeModel(
                ar_name = "مصر",
                en_name = "Egypt",
                code = "+20",
                flag = "EG"
            )
        }
    }

    override suspend fun getCountries(): List<CountryCodeModel>  = withContext(
        context = ioDispatcher + SupervisorJob()
    ) {
        try {
            val data = countriesService.getAllCountriesData().body()!!
            return@withContext CountriesUtil.getCountries(data)
        } catch (e: Exception) {
            return@withContext emptyList()
        }
    }
}