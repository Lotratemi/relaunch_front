package com.codingfactory.relaunch.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingfactory.relaunch.data.api.ApiClient
import com.codingfactory.relaunch.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val isLoading: Boolean = false,
    val userId: Long? = null,
    val userName: String = "",
    val error: String? = null
)

class OnboardingViewModel : ViewModel() {
    private val userRepo = UserRepository(ApiClient.apiService)

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun createUser(name: String, mail: String, age: String, sex: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val ageShort = age.toShortOrNull() ?: 0
            userRepo.createUser(name, mail, ageShort).fold(
                onSuccess = { user ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userId = user.id,
                            userName = user.name
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message)
                    }
                }
            )
        }
    }
}
