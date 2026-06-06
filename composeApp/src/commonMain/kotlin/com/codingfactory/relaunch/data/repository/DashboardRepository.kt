package com.codingfactory.relaunch.data.repository

import com.codingfactory.relaunch.data.api.ApiService
import com.codingfactory.relaunch.data.model.ObjectiveDto

class DashboardRepository(private val api: ApiService) {

    companion object {
        private var lastOpenedDay: Int = 0
        private var isDayCompleted: Boolean = false
    }

    fun getLastOpenedDay(): Int = lastOpenedDay
    fun saveLastOpenedDay(day: Int) { lastOpenedDay = day }
    fun disappointedStatus(): Boolean = isDayCompleted
    fun saveDayCompletionStatus(status: Boolean) { isDayCompleted = status }

    suspend fun getObjectives(userId: Long): Result<List<ObjectiveDto>> = runCatching {
        api.getObjectivesByUser(userId)
    }

    suspend fun deleteObjective(userId: Long, objectiveId: Long): Result<Unit> = runCatching {
        api.deleteObjective(userId, objectiveId)
    }

    suspend fun updateObjective(userId: Long, objectiveId: Long, objective: ObjectiveDto): Result<ObjectiveDto> = runCatching {
        api.updateObjective(userId, objectiveId, objective)
    }

}
