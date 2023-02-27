package org.myf.demo.core.model.countries

import kotlinx.serialization.Serializable

@Serializable
data class CountryID(
    val root: String? = null,
    val suffixes: List<String>? = emptyList()
)
