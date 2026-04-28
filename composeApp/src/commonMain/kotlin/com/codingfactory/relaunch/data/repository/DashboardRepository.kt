package com.codingfactory.relaunch.data.repository

import com.codingfactory.relaunch.data.api.ApiService
import com.codingfactory.relaunch.data.model.ObjectiveDto
import com.codingfactory.relaunch.data.model.StreakDto
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive

class DashboardRepository(private val api: ApiService) {

    suspend fun getObjectives(userId: Long): Result<List<ObjectiveDto>> = runCatching {
        api.getObjectivesByUser(userId)
    }

    suspend fun deleteObjective(userId: Long, objectiveId: Long): Result<Unit> = runCatching {
        api.deleteObjective(userId, objectiveId)
    }

    suspend fun getStreaks(userId: Long): Result<List<StreakDto>> = runCatching {
        api.getStreaksByUser(userId)
    }

    suspend fun createStreak(userId: Long, dayBitmask: Long): Result<StreakDto> = runCatching {
        api.createStreak(
            StreakDto(
                userId = userId,
                data = JsonArray(listOf(JsonPrimitive(dayBitmask)))
            )
        )
    }

    suspend fun updateStreak(streak: StreakDto): Result<StreakDto> = runCatching {
        api.updateStreak(streak.id!!, streak)
    }
}
