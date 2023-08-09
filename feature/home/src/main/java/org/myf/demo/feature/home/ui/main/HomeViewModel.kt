package org.myf.demo.feature.home.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.myf.demo.core.common.model.HomeModel
import org.myf.demo.core.data.repository.HomeRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: HomeRepository
): ViewModel(){

    private val _uiState = MutableStateFlow(HomeModel())
    val uiState: StateFlow<HomeModel> = _uiState
    fun getData() = viewModelScope.launch {
        repo.getData().collect{ home ->
            _uiState.update { home }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancelChildren()
        repo.cancelJob()
    }
}