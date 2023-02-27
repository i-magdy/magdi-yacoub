package org.myf.demo.core.data.util

import org.myf.demo.core.model.countries.CountryCodeModel
import org.myf.demo.core.model.countries.CountryModel

object CountriesUtil {

    fun getCountries(
        countries: List<CountryModel>
    ): List<CountryCodeModel> {
        val list = ArrayList<CountryCodeModel>()
        countries.forEach {
            val idd = it.idd
            if (idd != null) {
                list.add(
                    CountryCodeModel(
                        ar_name = it.translations["ara"]?.common ?: "",
                        en_name = it.name.common,
                        flag = it.flag,
                        code = idd.root +
                                if (idd.suffixes.isNullOrEmpty() || idd.suffixes?.size!! > 1) "" else
                                idd.suffixes?.get(0)
                    )
                )
            }
        }
        return list
    }
}