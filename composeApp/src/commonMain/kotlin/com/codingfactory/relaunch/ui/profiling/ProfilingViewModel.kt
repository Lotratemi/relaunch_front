package com.codingfactory.relaunch.ui.profiling

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingfactory.relaunch.data.api.ApiClient
import com.codingfactory.relaunch.data.repository.ProfilingRepository
import com.codingfactory.relaunch.data.model.ProfilingResponseDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfilingUiState(
    val isLoading: Boolean = false,
    val currentQuestion: Int = 0,
    val answers: Map<Int, String> = emptyMap(),
    val result: ProfilingResponseDto? = null,
    val error: String? = null
)

class ProfilingViewModel : ViewModel() {
    private val repo = ProfilingRepository(ApiClient.apiService)

    private val _uiState = MutableStateFlow(ProfilingUiState())
    val uiState: StateFlow<ProfilingUiState> = _uiState.asStateFlow()

    fun answer(questionIndex: Int, value: String) {
        _uiState.update {
            it.copy(answers = it.answers + (questionIndex to value))
        }
    }

    fun nextQuestion() {
        _uiState.update {
            it.copy(currentQuestion = it.currentQuestion + 1)
        }
    }

    fun submitProfiling(userId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repo.analyzeProfiling(userId, _uiState.value.answers).fold(
                onSuccess = { result ->
                    _uiState.update {
                        it.copy(isLoading = false, result = result)
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