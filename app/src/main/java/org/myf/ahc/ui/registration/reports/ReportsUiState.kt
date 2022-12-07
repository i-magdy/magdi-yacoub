package org.myf.ahc.ui.registration.reports

import org.myf.ahc.models.DocumentModel

data class ReportsUiState(
    val pickImage: Boolean = false,
    val pickFile: Boolean = false,
    val list: List<DocumentModel> = emptyList(),
    val size: Long = 0L,
    val isLoading: Boolean = true,
    val isEmpty: Boolean = false,
    val fileName: String = "",
    val progress: Int = 0,
    val deleteFile: DocumentModel? = null
)
