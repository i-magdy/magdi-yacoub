package org.myf.ahc.ui.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.myf.ahc.core.datastore.DatastoreImpl

import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val datastore: DatastoreImpl
): ViewModel() {

    val state = datastore.state

    fun updateState(i: Int) = viewModelScope.launch {
        datastore.updateState(i)
    }

    fun updateAppLanguage(lang: String) = viewModelScope.launch {
        datastore.updateAppLanguage(lang)
    }

}