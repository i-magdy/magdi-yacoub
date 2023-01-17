package org.myf.ahc.core.datastore

data class PatientModel(
    val name: String = "",
    val id: String = "",
    val email: String = "",
    val img: String = "",
    val primaryPhone: String = "",
    val secondaryPhone: String = "",
    val isVerified: Boolean = false,
    val deviceToken: String = "",
    val fileCount: Int = 0
    )
