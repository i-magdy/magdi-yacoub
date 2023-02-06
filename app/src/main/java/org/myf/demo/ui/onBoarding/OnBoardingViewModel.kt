package org.myf.demo.ui.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.myf.demo.core.datastore.DatastoreImpl

import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val datastore: DatastoreImpl
): ViewModel() {

    private val _appLanguage = MutableStateFlow("")
    val appLanguage: StateFlow<String> = _appLanguage
    val state = datastore.state

    init {
        viewModelScope.launch {
            datastore.appLanguage.collect{
                _appLanguage.emit(it)
            }
        }
    }

    fun updateState(i: Int) = viewModelScope.launch {
        datastore.updateState(i)
    }

    fun updateAppLanguage(lang: String) = viewModelScope.launch {
        datastore.updateAppLanguage(lang)
    }

}