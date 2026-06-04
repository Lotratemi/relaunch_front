package com.codingfactory.relaunch.data.repository

import com.codingfactory.relaunch.data.api.ApiService
import com.codingfactory.relaunch.data.model.ObjectiveDto

class DashboardRepository(private val api: ApiService) {

    suspend fun getObjectives(userId: Long): Result<List<ObjectiveDto>> = runCatching {
        api.getObjectivesByUser(userId)
    }

    suspend fun deleteObjective(userId: Long, objectiveId: Comparable<*>): Result<Unit> = runCatching {
        api.deleteObjective(userId, objectiveId)
    }
}
