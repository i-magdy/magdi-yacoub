package org.myf.demo.feature.home.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import org.myf.demo.core.common.uiState.HomeUiState
import org.myf.demo.core.data.repository.HomeRepository
import org.myf.demo.core.datastore.DatastoreImpl
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: HomeRepository,
    private val datastore: DatastoreImpl
): ViewModel(){

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState
    private val _appLanguage = MutableStateFlow("")
    val appLanguage: StateFlow<String> = _appLanguage

    init {
        viewModelScope.launch {
            datastore.appLanguage.collect{
                _appLanguage.emit(it)
            }
        }
    }

    fun getData() = viewModelScope.launch {
        repo.getData().collect{ home ->
            _uiState.getAndUpdate {
                it.copy(model = home)
            }
        }
    }

    fun updateAppLanguage(
        lang: String
    ) = viewModelScope.launch {
        datastore.updateAppLanguage(lang)
    }

    fun openHomeSettingDialog() = viewModelScope.launch {
        _uiState.getAndUpdate {
            it.copy(
                isDialogOpen = true,
                isDialogClosed = false
            )
        }
    }

    fun closeHomeSettingDialog() = viewModelScope.launch {
        _uiState.getAndUpdate {
            it.copy(
                isDialogOpen = false,
                isDialogClosed = true
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancelChildren()
        repo.cancelJob()
    }
}