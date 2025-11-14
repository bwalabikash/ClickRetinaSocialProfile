package com.clickretina.android.clickretinasocialprofile.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clickretina.android.clickretinasocialprofile.data.model.ProfileResponseModel
import com.clickretina.android.clickretinasocialprofile.data.remote.api.RetrofitInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val profileResponse: ProfileResponseModel) : UiState()
    data class Error(val message: String) : UiState()
}

class ProfileViewModel(
    private val repository: IProfileRepository = ProfileRepository(RetrofitInstance.apiService)
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // optional one-time error event for SnackBar
    private val _errorEvent = MutableStateFlow<String?>(null)
    val errorEvent: StateFlow<String?> = _errorEvent.asStateFlow()

    private var job: Job? = null

    init {
        loadProfile()
    }

    fun loadProfile() {
        job?.cancel()
        job = viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = repository.fetchProfile()) {
                is RepoResult.Success -> _uiState.value = UiState.Success(result.data)
                is RepoResult.Error -> {
                    _uiState.value = UiState.Error(result.message)
                    _errorEvent.value = result.message
                }
                RepoResult.Loading -> _uiState.value = UiState.Loading
            }
        }
    }

    fun retry() {
        loadProfile()
    }

    fun clearErrorEvent() {
        _errorEvent.value = null
    }
}