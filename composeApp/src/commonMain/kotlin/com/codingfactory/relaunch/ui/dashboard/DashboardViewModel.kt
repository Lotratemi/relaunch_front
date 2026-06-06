@file:OptIn(kotlin.time.ExperimentalTime::class)
package com.codingfactory.relaunch.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingfactory.relaunch.data.api.ApiClient
import com.codingfactory.relaunch.data.model.ObjectiveDto
import com.codingfactory.relaunch.data.repository.DashboardRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Clock

private val _timeString = Clock.System.now().toString()

data class DashboardUiState(
    val objectives: List<Objective> = emptyList(),
    val trackedDays: Set<Int> = emptySet(),
    val wrongTrackedDays: Set<Int> = emptySet(),
    val displayedMonth: Int = _timeString.substring(5, 7).toInt(),
    val displayedYear: Int = _timeString.substring(0, 4).toInt(),
    val currentDay: Int = _timeString.substring(8, 10).toInt(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class DashboardViewModel(
    private val userId: Long,
    val userName: String
) : ViewModel() {

    private val todayDay: Int = _timeString.substring(8, 10).toInt()
    private val dashboardRepo = DashboardRepository(ApiClient.apiService)
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val _congratScreen = MutableStateFlow<String?>(null)
    val congratScreen: StateFlow<String?> = _congratScreen.asStateFlow()

    init {
        checkYestardayStatus()
        loadObjectives()
    }

    private fun checkYestardayStatus(){
        val lastOpenedDay = dashboardRepo.getLastOpenedDay()
        val wasLastDayCompleted = dashboardRepo.disappointedStatus()

        if (lastOpenedDay != 0 && lastOpenedDay != todayDay) {
            if (!wasLastDayCompleted){
                _congratScreen.value = "FAIL"
            }
        }

        dashboardRepo.saveLastOpenedDay(todayDay)
        dashboardRepo.saveDayCompletionStatus(false)
    }

    private fun loadObjectives() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            dashboardRepo.getObjectives(userId).fold(
                onSuccess = { dtos ->
                    val objectives = dtos.mapIndexed { index, dto ->
                        Objective(
                            id = dto.id?.toInt() ?: (index + 1),
                            title = dto.title,
                            isCheck = dto.isCompleted,
                            endAt = dto.endAt,
                            frequency = dto.frequency
                        )
                    }

                    _uiState.update { state ->
                        val allChecked = objectives.isNotEmpty() && objectives.all { it.isCheck }
                        val anyChecked = objectives.any { it.isCheck }

                        val tracked = if (allChecked) state.trackedDays + todayDay else state.trackedDays - todayDay
                        val wrong = if (allChecked) state.wrongTrackedDays - todayDay
                        else if (!anyChecked) state.wrongTrackedDays + todayDay
                        else state.wrongTrackedDays - todayDay

                        dashboardRepo.saveDayCompletionStatus(allChecked)

                        state.copy(
                            objectives = objectives,
                            trackedDays = tracked,
                            wrongTrackedDays = wrong,
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
        val currentState = _uiState.value
        val obj = currentState.objectives[index]
        val newIsCheck = !obj.isCheck

        viewModelScope.launch {
            val dto = ObjectiveDto(
                id = obj.id.toLong(),
                userId = userId,
                title = obj.title,
                endAt = obj.endAt,
                frequency = obj.frequency,
                isCompleted = newIsCheck
            )
            dashboardRepo.updateObjective(userId, obj.id.toLong(), dto)
        }

        _uiState.update { state ->
            val updated = state.objectives.toMutableList()
            val obj = updated[index]
            updated[index] = obj.copy(isCheck = !obj.isCheck)

            val allChecked = updated.isNotEmpty() && updated.all { it.isCheck }
            val anyChecked = updated.any { it.isCheck }

            if (allChecked && !state.trackedDays.contains(todayDay)){
                _congratScreen.value = "SUCCESS"
            }

            dashboardRepo.saveDayCompletionStatus(allChecked)

            val tracked = if (allChecked) {
                state.trackedDays + todayDay
            } else {
                state.trackedDays - todayDay
            }
            val wrong = if (allChecked) {
                state.wrongTrackedDays - todayDay
            } else if (!anyChecked) {
                state.wrongTrackedDays + todayDay
            } else {
                state.wrongTrackedDays - todayDay
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
            dashboardRepo.deleteObjective(userId, obj.id.toLong())
        }
        _uiState.update { state ->
            state.copy(objectives = state.objectives.toMutableList().also { it.removeAt(index) })
        }
    }

    fun dismissCongratsScreen() {
        _congratScreen.value = null
    }
}