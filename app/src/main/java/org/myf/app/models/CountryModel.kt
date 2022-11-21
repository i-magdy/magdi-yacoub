package org.myf.app.models

data class CountryModel(
    val name: CountryNameModel,
    val idd: CountryID?,
    val cca2: String,
    val flag: String,
    val translations: Map<String,CountryTranslations>
)