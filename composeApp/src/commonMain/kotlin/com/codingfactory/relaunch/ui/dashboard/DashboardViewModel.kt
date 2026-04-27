package com.codingfactory.relaunch.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingfactory.relaunch.data.api.ApiClient
import com.codingfactory.relaunch.data.repository.DashboardRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DashboardUiState(
    val objectives: List<Objective> = emptyList(),
    val trackedDays: Set<Int> = setOf(7, 8, 9, 10, 11, 12, 13, 17),
    val wrongTrackedDays: Set<Int> = setOf(6, 14, 15, 16),
    val isLoading: Boolean = true,
    val error: String? = null
)

class DashboardViewModel(
    private val userId: Long,
    val userName: String
) : ViewModel() {

    private val dashboardRepo = DashboardRepository(ApiClient.apiService)
    private val today = 18

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadObjectives()
    }

    private fun loadObjectives() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            dashboardRepo.getObjectives(userId).fold(
                onSuccess = { dtos ->
                    val objectives = dtos.mapIndexed { index, dto ->
                        Objective(
                            id = dto.id?.toInt() ?: (index + 1),
                            title = dto.title
                        )
                    }
                    _uiState.update {
                        it.copy(
                            objectives = objectives,
                            isLoading = false
                        )
                    }
                },
                onFailure = { e ->
                    // Fallback to defaults if backend unreachable
                    _uiState.update {
                        it.copy(
                            objectives = defaultObjectives(),
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
            )
        }
    }

    fun toggleObjective(index: Int) {
        _uiState.update { state ->
            val updated = state.objectives.toMutableList()
            val obj = updated[index]
            updated[index] = obj.copy(isCheck = !obj.isCheck)

            val allChecked = updated.all { it.isCheck }
            val anyChecked = updated.any { it.isCheck }

            val tracked = if (allChecked) {
                state.trackedDays + today
            } else {
                state.trackedDays - today
            }
            val wrong = if (allChecked) {
                state.wrongTrackedDays - today
            } else if (!anyChecked) {
                state.wrongTrackedDays + today
            } else {
                state.wrongTrackedDays - today
            }

            state.copy(
                objectives = updated,
                trackedDays = tracked,
                wrongTrackedDays = wrong
            )
        }
    }

    fun deleteObjective(index: Int) {
        val obj = _uiState.value.objectives.getOrNull(index) ?: return
        viewModelScope.launch {
            dashboardRepo.deleteObjective(obj.id.toLong())
        }
        _uiState.update { state ->
            state.copy(objectives = state.objectives.toMutableList().also { it.removeAt(index) })
        }
    }

    private fun defaultObjectives() = listOf(
        Objective(id = 1, title = "Prendre sa douche (21h)"),
        Objective(id = 2, title = "Se laver les dents (21h25)"),
        Objective(id = 3, title = "Se mettre un réveil (21h30)"),
        Objective(id = 4, title = "Se coucher plus tôt (22h)"),
    )
}
