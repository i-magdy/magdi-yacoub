package org.myf.ahc.core.data.util

import org.myf.ahc.core.model.countries.CountryCodeModel
import org.myf.ahc.core.model.countries.CountryModel

object CountriesUtil {

    fun getCountries(
        countries: List<CountryModel>
    ): List<CountryCodeModel>{
        return if (countries.isEmpty()){
            emptyList()
        }else{
            val list = ArrayList<CountryCodeModel>()
            countries.forEach {
                list.add(
                    CountryCodeModel(
                        ar_name = it.translations["ara"]?.common ?: "",
                        en_name = it.name.common,
                        flag = it.flag,
                        code = if(it.idd == null) "" else it.idd!!.root + if (it.idd!!.suffixes.isNullOrEmpty() || it.idd!!.suffixes?.size!! > 1) "" else it.idd!!.suffixes?.get(0)
                    )
                )
            }
            list
        }
    }
}