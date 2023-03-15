package org.myf.demo.feature.registration.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.myf.demo.core.common.uiState.RegistrationUiState
import org.myf.demo.core.data.repository.CurrentPatientRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject  constructor(
    private val repo: CurrentPatientRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState

    fun sync() = viewModelScope.launch{
        _uiState.update { it.copy(isLoading = false, isUserExist = repo.getPatientAsync().await()) }
    }

}