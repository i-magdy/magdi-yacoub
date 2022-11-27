package org.myf.ahc.ui.registration.verify


import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.myf.ahc.api.CountriesService
import org.myf.ahc.models.CountryCodeModel
import org.myf.ahc.models.CountryModel
import org.myf.ahc.util.VerifyUtil
import retrofit2.Response
import javax.inject.Inject

class VerifyRepo @Inject constructor(
    private val countryService: CountriesService,
    private val util: VerifyUtil
) {

    private val coroutine = CoroutineScope(Dispatchers.Default)
    val selectedCountry = MutableStateFlow(CountryCodeModel())
    val countriesName = MutableStateFlow<List<String>>(emptyList())

    init {
        coroutine.launch {
            launch { getCountries() }
        }
    }

    suspend fun getCountryByCode(code: String){
        var response: Response<List<CountryModel>>? = countryService.getCountryByCode(code)
        if (response != null) {
            if (response.isSuccessful &&
                response.body() != null &&
                response.body()!!.isNotEmpty()
            ) {
                setSelectedCountry(
                    country = CountryCodeModel(
                        ar_name = response.body()!![0].translations["ara"]?.common ?: "",
                        en_name = response.body()!![0].name.common,
                        code = if (null == response.body()!![0].idd) "" else response.body()!![0].idd!!.root+
                                if (response.body().isNullOrEmpty()) "" else response.body()!![0].idd!!.suffixes!![0],
                        flag = response.body()!![0].flag
                    )
                )
                selectedCountry.emit(util.selectedCountry!!)
                response = null
            }
        }
    }

    fun setSelectedCountryByName(
        name: String
    ) = coroutine.launch {
        if (util.appLang == "ar"){
            if (util.selectedCountry!!.ar_name == name){
                return@launch
            }
        }else{
            if (util.selectedCountry!!.en_name == name){
                return@launch
            }
        }
        Log.e("name",name)
        util.getCountries().forEach {
            if (util.appLang == "ar"){
                if (it.ar_name == name){
                    setSelectedCountry(it)
                }
            }else{
                if (it.en_name == name){
                    setSelectedCountry(it)
                }
            }
        }
        selectedCountry.emit(util.selectedCountry!!)
    }

    private suspend fun getCountries(){
        var response: Response<List<CountryModel>>? = countryService.getAllCountriesData()
        if (response != null){
            if (response.isSuccessful &&
                response.body() != null &&
                response.body()!!.isNotEmpty()){
                util.setCountries(response.body()!!)
                val list = ArrayList<String>()
                util.getCountries().forEach {
                    if (util.appLang == "ar"){
                        list.add(it.ar_name)
                    }else{
                        list.add(it.en_name)
                    }
                }
                countriesName.emit(list)
                response = null
            }
        }
    }

    private fun setSelectedCountry(
        country: CountryCodeModel
    ) = util.setSelectedCountry(
        countryModel = country
    )

    fun setAppLang(lang: String){
        util.appLang = lang
    }
}