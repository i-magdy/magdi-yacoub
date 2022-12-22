package org.myf.ahc.util

import org.myf.ahc.models.CountryCodeModel
import org.myf.ahc.models.CountryModel
import javax.inject.Inject

class CountriesUtil @Inject constructor() {

    var appLang = ""
    private var _selectedCountry: CountryCodeModel? = null
    private var _countries: List<CountryModel> = emptyList()
    val selectedCountry: CountryCodeModel? get() = _selectedCountry



    fun setSelectedCountry(countryModel: CountryCodeModel){
        _selectedCountry = countryModel
    }

    fun setCountries(countries: List<CountryModel>){
        _countries = countries
    }

    fun getCountries(): List<CountryCodeModel>{
        return if (_countries.isEmpty()){
            emptyList()
        }else{
            val list = ArrayList<CountryCodeModel>()
            _countries.forEach {
                list.add(
                    CountryCodeModel(
                        ar_name = it.translations["ara"]?.common ?: "",
                        en_name = it.name.common,
                        flag = it.flag,
                        code = if(it.idd == null) "" else it.idd.root + if (it.idd.suffixes.isNullOrEmpty()) "" else it.idd.suffixes[0]
                    )
                )
            }
            list
        }
    }
}