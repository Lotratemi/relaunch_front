package com.codingfactory.relaunch.ui.onboarding

import androidx.lifecycle.*
import com.codingfactory.relaunch.data.api.ApiClient
import com.codingfactory.relaunch.data.repository.UserRepository
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.io.IOException


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
        _uiState.update { it.copy(error = null) }

        if (name.isBlank()) {
            _uiState.update { it.copy(error = "Nom obligatoire") }
            return
        }

        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        if (mail.isNotBlank() || !mail.matches(emailRegex)) {
            _uiState.update { it.copy(error = "E-mail non valide") }
            return
        }

        val ageI = age.toIntOrNull()
        if (ageI == null || ageI !in 1..120) {
            _uiState.update { it.copy(error = "Age non valide : (1-120)") }
        }

        if (sex.isNotBlank()){
            _uiState.update { it.copy( error = "Selectionnez un genre") }
            return
        }

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
                    val messageClient = when (e) {
                        is HttpRequestTimeoutException, is ConnectTimeoutException, is SocketTimeoutException ->
                            "Problème serveur : Réessyez plus tard"
                        is IOException ->
                            "Probème connexion : vérifiez votre internet"
                        else -> {
                            if (e.message?.contains("503") == true) {
                                "Le serveur de base de données est en maintenance"
                            } else if (e.message?.contains("401") == true) {
                                "Erreur d'accès à l'IA"
                            } else {
                                "Problème technique inconnu"
                            }
                        }                    }
                    _uiState.update {
                        it.copy(isLoading = false, error = messageClient)
                    }
                }
            )
        }
    }
}
