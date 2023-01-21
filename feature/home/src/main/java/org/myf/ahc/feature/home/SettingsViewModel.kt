package org.myf.ahc.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.myf.ahc.core.datastore.DatastoreImpl
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val datastore: DatastoreImpl
): ViewModel(){

    fun updateAppLanguage(lang: String) = viewModelScope.launch {
        datastore.updateAppLanguage(lang)
    }

}