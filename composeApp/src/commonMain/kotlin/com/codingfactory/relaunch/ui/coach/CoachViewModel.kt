package com.codingfactory.relaunch.ui.coach

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingfactory.relaunch.data.api.ApiClient
import com.codingfactory.relaunch.data.repository.CoachRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CoachUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isTyping: Boolean = false,
    val isResponding: Boolean = false,
    val showConfirm: Boolean = false,
    val inputEnabled: Boolean = true
)

class CoachViewModel(
    private val userId: Long,
    private val userName: String
) : ViewModel() {

    private val coachRepo = CoachRepository(ApiClient.apiService)
    private var mistralConvId: String? = null
    private var pendingObjectives: List<String> = emptyList()

    private val _uiState = MutableStateFlow(CoachUiState(isResponding = true, inputEnabled = false))
    val uiState: StateFlow<CoachUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            coachRepo.startCoachConversation(userId)
                .onSuccess {
                    mistralConvId = it.mistralConvId
                    _uiState.update { s -> s.copy(isResponding = false, inputEnabled = true) }
                }
                .onFailure {
                    _uiState.update { s ->
                        s.copy(
                            isResponding = false,
                            inputEnabled = false,
                            messages = s.messages + ChatMessage(
                                "Désolé $userName, je n'arrive pas à démarrer la conversation. Réessaie plus tard.",
                                isUser = false
                            )
                        )
                    }
                }
        }
    }

    fun sendMessage(text: String) {
        val trimmed = text.trim()
        val convId = mistralConvId ?: return
        if (trimmed.isBlank() || _uiState.value.isResponding) return
        if (_uiState.value.showConfirm) return

        val userMsg = ChatMessage(trimmed, isUser = true)
        _uiState.update {
            it.copy(
                messages = it.messages + userMsg,
                isResponding = true,
                isTyping = true,
                inputEnabled = false
            )
        }

        viewModelScope.launch {
            coachRepo.sendCoachMessage(userId, convId, trimmed)
                .onSuccess { reply ->
                    val content = reply.response.ifBlank { "…" }
                    if (reply.objectives.isNotEmpty()) {
                        pendingObjectives = reply.objectives
                    }
                    val confirm = reply.objectivesCreationTrigger && pendingObjectives.isNotEmpty()

                    _uiState.update {
                        it.copy(
                            messages = it.messages + ChatMessage(content, isUser = false),
                            isTyping = false,
                            isResponding = false,
                            inputEnabled = !confirm,
                            showConfirm = confirm
                        )
                    }
                }
                .onFailure { err ->
                    _uiState.update {
                        it.copy(
                            messages = it.messages + ChatMessage(
                                "Désolé, une erreur est survenue. Réessaie.\n[debug] ${err::class.simpleName}: ${err.message}",
                                isUser = false
                            ),
                            isTyping = false,
                            isResponding = false,
                            inputEnabled = true
                        )
                    }
                }
        }
    }

    fun canShowInput(): Boolean = !_uiState.value.showConfirm

    fun confirmObjectives(onDone: () -> Unit) {
        viewModelScope.launch {
            if (pendingObjectives.isNotEmpty()) {
                coachRepo.createObjectivesForUser(userId, pendingObjectives)
            }
            onDone()
        }
    }

    fun declineObjectives() {
        pendingObjectives = emptyList()
        _uiState.update { it.copy(showConfirm = false, inputEnabled = true) }
    }
}
