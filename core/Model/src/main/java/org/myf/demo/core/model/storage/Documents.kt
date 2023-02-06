package org.myf.demo.core.model.storage

data class Documents(
    val documents: List<DocumentModel> = emptyList(),
    val failed: Boolean = false,
    val totalSize: Long = 0L
)
