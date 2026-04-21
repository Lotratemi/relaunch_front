package com.codingfactory.relaunch.ui.coach

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingfactory.relaunch.data.api.ApiClient
import com.codingfactory.relaunch.data.repository.CoachRepository
import kotlinx.coroutines.delay
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
    private var conversationId: Long? = null
    private var mistralConvId: String? = null
    private var turn = 0

    val totalResponses: Int = MAX_TURNS

    private val _uiState = MutableStateFlow(CoachUiState(isResponding = true, inputEnabled = false))
    val uiState: StateFlow<CoachUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            coachRepo.startCoachConversation(userId)
                .onSuccess {
                    conversationId = it.id
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
        if (turn >= MAX_TURNS) return

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
            conversationId?.let { id -> coachRepo.saveMessage(id, trimmed, isUser = true) }

            coachRepo.sendCoachMessage(userId, convId, trimmed)
                .onSuccess { reply ->
                    val content = reply.ifBlank { "…" }
                    turn++
                    val isLast = turn >= MAX_TURNS
                    _uiState.update {
                        it.copy(
                            messages = it.messages + ChatMessage(content, isUser = false),
                            isTyping = false,
                            isResponding = false,
                            inputEnabled = !isLast
                        )
                    }
                    conversationId?.let { id -> coachRepo.saveMessage(id, content, isUser = false) }

                    if (isLast) {
                        delay(400)
                        finishConversation()
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            messages = it.messages + ChatMessage(
                                "Désolé, une erreur est survenue. Réessaie.",
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

    fun canShowInput(): Boolean = turn < MAX_TURNS && !_uiState.value.showConfirm

    private fun finishConversation() {
        if (_uiState.value.showConfirm) return
        _uiState.update { it.copy(showConfirm = true, inputEnabled = false) }

        viewModelScope.launch {
            conversationId?.let { id -> coachRepo.markConversationDone(id, userId) }
            val titles = listOf(
                "Prendre sa douche (21h)",
                "Se laver les dents (21h25)",
                "Se mettre un réveil (21h30)",
                "Se coucher plus tôt (22h)"
            )
            coachRepo.createObjectivesForUser(userId, titles)
        }
    }

    companion object {
        private const val MAX_TURNS = 3
    }
}
