package org.myf.demo.core.model.countries

import kotlinx.serialization.Serializable

@Serializable
data class CountryModel(
    val name: CountryNameModel,
    val idd: CountryID? = null,
    val cca2: String,
    val flag: String,
    val translations: Map<String,CountryTranslations>
)