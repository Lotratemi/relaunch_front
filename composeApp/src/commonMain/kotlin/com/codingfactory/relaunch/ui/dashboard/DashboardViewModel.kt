package com.codingfactory.relaunch.ui.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingfactory.relaunch.data.repository.DashboardRepository
import io.ktor.util.date.WeekDay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DashboardUiState(
    val objectives: List<Objective> = emptyList(),
    val trackedDays: Set<Int> = emptySet(),
    val wrongTrackedDays: Set<Int> = emptySet(),
    val isLoading: Boolean = true,
    val error: String? = null
    )

class DashboardViewModel(
    private val userId: Long,
    val userName: String,
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    private val _selectDay = mutableStateOf(1)
    val selectedDay: State<Int> = _selectDay
    private val _congratScreen = mutableStateOf<String?>(null)
    val  congratScreen : State<String?> = _congratScreen

    init {
        loadObjectives()
    }

    fun selectDay(day: Int) {
        _selectDay.value = day
    }

    private fun loadObjectives() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            dashboardRepository.getObjectives(userId).fold(
                onSuccess = { dtos ->
                    val objectives = dtos.mapIndexed { index, dto ->
                        Objective(
                            id = dto.id ?: (index + 1).toString(),
                            title = dto.title,
                            isCheck = dto.isCompleted
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
                    _uiState.update {
                        it.copy(
                            objectives = emptyList(),
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

            val toggledObj
            updated[index] = obj.copy(isCheck = !obj.isCheck)


            val allChecked = updated.all { it.isCheck }
            val anyChecked = updated.any { it.isCheck }
            val currentDay =  _selectDay.value

            val tracked = if (allChecked) {
                state.trackedDays + currentDay
            } else {
                state.trackedDays - currentDay
            }
            val wrong = if (allChecked) {
                state.wrongTrackedDays - currentDay
            } else if (!anyChecked) {
                state.wrongTrackedDays + currentDay
            } else {
                state.wrongTrackedDays - currentDay
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
            dashboardRepository.deleteObjective(userId, obj.id)
        }
        _uiState.update { state ->
            state.copy(objectives = state.objectives.toMutableList().also { it.removeAt(index) })
        }
    }

    fun valideCurrentDay (){
        val currentObjectives = _uiState.value.objectives
        if (currentObjectives.isEmpty()) {
            _congratScreen.value = "FAIL"
            return
        }

        val allCompleted = currentObjectives.all { it.isCheck }
        if (allCompleted) {
            _congratScreen.value = "SUCCESS"
        } else {
            _congratScreen.value = "FAIL"
        }
    }

    fun dismissCongratsScreen() {
        _congratScreen.value = null
    }
}
