package org.myf.ahc.core.model.storage

data class DocumentModel(
    val path: String,
    val name: String,
    val type: String,
    val note: String?,
    val size: Long
)
