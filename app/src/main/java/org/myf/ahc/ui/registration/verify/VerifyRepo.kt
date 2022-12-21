package org.myf.ahc.ui.registration.verify


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.myf.ahc.api.CountriesService
import org.myf.ahc.data.DatastoreImpl
import org.myf.ahc.models.CountryCodeModel
import org.myf.ahc.models.CountryModel
import org.myf.ahc.util.CountriesUtil
import retrofit2.Response
import javax.inject.Inject

class VerifyRepo @Inject constructor(
    private val countryService: CountriesService,
    private val util: CountriesUtil,
    private val datastore: DatastoreImpl
) {

    private val coroutine = CoroutineScope(Dispatchers.Default)
    val selectedCountry = MutableStateFlow(CountryCodeModel())
    val countriesName = MutableStateFlow<List<String>>(emptyList())

    suspend fun getCountryByCode(code: String) {
        val response: Response<List<CountryModel>> = countryService.getCountryByCode(code)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                setSelectedCountry(
                    country = CountryCodeModel(
                        ar_name = body[0].translations["ara"]?.common ?: "",
                        en_name = body[0].name.common,
                        code = if (null == body[0].idd) "" else body[0].idd?.root +
                                if (body.isEmpty()) "" else
                                    body[0].idd?.suffixes?.get(0) ?: "",
                        flag = body[0].flag
                    )
                )
                selectedCountry.emit(util.selectedCountry!!)
            }
        }
    }

    fun setSelectedCountryByName(
        name: String
    ) = coroutine.launch {
        if(util.selectedCountry != null) {
            if (util.appLang == "ar") {
                if (util.selectedCountry!!.ar_name == name) {
                    return@launch
                }
            } else {
                if (util.selectedCountry!!.en_name == name) {
                    return@launch
                }
            }
        }
        util.getCountries().forEach {
            if (util.appLang == "ar") {
                if (it.ar_name == name) {
                    setSelectedCountry(it)
                }
            } else {
                if (it.en_name == name) {
                    setSelectedCountry(it)
                }
            }
        }
        selectedCountry.emit(util.selectedCountry!!)
    }

    suspend fun getCountries() {
        val response: Response<List<CountryModel>> = countryService.getAllCountriesData()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                util.setCountries(body)
                val list = ArrayList<String>()
                util.getCountries().forEach { country ->
                    if (util.appLang == "ar") {
                        list.add(country.ar_name)
                        list.sortBy { s -> s}
                    } else {
                        list.add(country.en_name)
                        list.sortBy { s -> s}
                    }
                }
                countriesName.emit(list)
            }
        }
    }

    private fun setSelectedCountry(
        country: CountryCodeModel
    ) = util.setSelectedCountry(
        countryModel = country
    )

    fun setAppLang(lang: String) {
        util.appLang = lang
    }


    fun updateStateForLogin() = coroutine.launch {
        datastore.updateState(2)
    }

    fun cancelJobs() = coroutine.cancel()
}