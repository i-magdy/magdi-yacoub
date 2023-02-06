package org.myf.demo.core.model.storage

data class DocumentModel(
    val path: String,
    val name: String,
    val type: String,
    val note: String?,
    val size: Long,
    val url: String
)
