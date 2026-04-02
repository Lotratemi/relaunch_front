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
    private var responseStep = 0

    private val responses = listOf(
        listOf(
            "Ne t'inquiète pas $userName, on va trouver une solution réalisable !",
            "As tu déjà identifié le ou les éléments qui rabaissent ton moral ?"
        ),
        listOf(
            "Je comprends. Le boulot c'est toujours un puit sans fond de fatigue.",
            "À quelle heure te couches tu et à quelle heure te réveilles tu ?"
        ),
        listOf(
            "Je vois le problème ! Un manque de sommeil peut tout dérégler.",
            "Je te propose de commencer par te coucher à 22h. Je vais créer cet objectif pour toi !"
        )
    )

    val totalResponses: Int = responses.size

    private val _uiState = MutableStateFlow(CoachUiState())
    val uiState: StateFlow<CoachUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            coachRepo.createConversation(userId).onSuccess {
                conversationId = it.id
            }
        }
    }

    fun sendMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isBlank() || _uiState.value.isResponding) return

        val userMsg = ChatMessage(trimmed, isUser = true)
        _uiState.update { it.copy(messages = it.messages + userMsg) }

        // Persist user message
        viewModelScope.launch {
            conversationId?.let { id ->
                coachRepo.saveMessage(id, trimmed, isUser = true)
            }
        }

        if (responseStep < responses.size) {
            val currentResponses = responses[responseStep]
            val isLast = responseStep == responses.size - 1
            responseStep++
            _uiState.update { it.copy(isResponding = true, inputEnabled = false) }

            viewModelScope.launch {
                delay(600)
                _uiState.update { it.copy(isTyping = true) }
                delay(1200)
                _uiState.update { it.copy(isTyping = false) }

                val msg1 = ChatMessage(currentResponses[0], isUser = false)
                _uiState.update { it.copy(messages = it.messages + msg1) }
                persistCoachMessage(currentResponses[0])

                if (currentResponses.size > 1) {
                    delay(500)
                    _uiState.update { it.copy(isTyping = true) }
                    delay(1000)
                    _uiState.update { it.copy(isTyping = false) }

                    val msg2 = ChatMessage(currentResponses[1], isUser = false)
                    _uiState.update { it.copy(messages = it.messages + msg2) }
                    persistCoachMessage(currentResponses[1])
                }

                if (isLast) {
                    delay(400)
                    _uiState.update { it.copy(showConfirm = true) }
                    saveObjectives()
                }

                _uiState.update {
                    it.copy(
                        isResponding = false,
                        inputEnabled = responseStep < responses.size
                    )
                }
            }
        }
    }

    fun canShowInput(): Boolean = responseStep < responses.size

    private suspend fun persistCoachMessage(content: String) {
        conversationId?.let { id ->
            coachRepo.saveMessage(id, content, isUser = false)
        }
    }

    private suspend fun saveObjectives() {
        conversationId?.let { id ->
            coachRepo.markConversationDone(id, userId)
        }
        val titles = listOf(
            "Prendre sa douche (21h)",
            "Se laver les dents (21h25)",
            "Se mettre un réveil (21h30)",
            "Se coucher plus tôt (22h)"
        )
        coachRepo.createObjectivesForUser(userId, titles)
    }
}
