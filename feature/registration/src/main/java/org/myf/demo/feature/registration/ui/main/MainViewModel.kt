package org.myf.demo.feature.registration.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.myf.demo.core.datastore.DatastoreImpl
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject  constructor(
    private val datastoreImpl: DatastoreImpl
): ViewModel() {


    fun sync() = viewModelScope.launch{
        datastoreImpl.state.collect{
            Log.e("State","$it")
        }
    }
}