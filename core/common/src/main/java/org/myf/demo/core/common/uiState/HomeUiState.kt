package org.myf.demo.core.common.uiState

import org.myf.demo.core.common.model.HomeModel

data class HomeUiState(
    val model: HomeModel = HomeModel(),
    val isDialogClosed: Boolean = true,
    val isDialogOpen: Boolean = false
)
