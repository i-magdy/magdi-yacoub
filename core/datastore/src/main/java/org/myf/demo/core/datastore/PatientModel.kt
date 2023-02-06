package org.myf.demo.core.datastore

import android.net.Uri

data class PatientModel(
    val name: String = "",
    val id: String = "",
    val email: String = "",
    val img: Uri = Uri.EMPTY,
    val primaryPhone: String = "",
    val secondaryPhone: String = "",
    val isVerified: Boolean = false,
    val deviceToken: String = "",
    val fileCount: Int = 0
    )
