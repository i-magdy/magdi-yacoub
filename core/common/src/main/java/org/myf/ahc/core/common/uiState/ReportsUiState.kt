package org.myf.ahc.core.common.uiState

import org.myf.ahc.core.model.storage.DocumentModel


data class ReportsUiState(
    val pickImage: Boolean = false,
    val pickFile: Boolean = false,
    val list: List<DocumentModel> = emptyList(),
    val size: Long = 0L,
    val isLoading: Boolean = true,
    val isEmpty: Boolean = false,
    val fileName: String = "",
    val progress: Int = 0,
    val deleteFile: DocumentModel? = null,
    val isUploading: Boolean = false
)
