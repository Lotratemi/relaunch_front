package com.codingfactory.relaunch.data.repository

import com.codingfactory.relaunch.data.api.ApiService
import com.codingfactory.relaunch.data.model.CoachConversationDto
import com.codingfactory.relaunch.data.model.CoachReplyDto
import com.codingfactory.relaunch.data.model.ObjectiveDto

class CoachRepository(private val api: ApiService) {

    suspend fun startCoachConversation(userId: Long): Result<CoachConversationDto> = runCatching {
        api.startCoachConversation(userId)
    }

    suspend fun sendCoachMessage(
        userId: Long,
        mistralConvId: String,
        message: String
    ): Result<CoachReplyDto> = runCatching {
        api.sendCoachMessage(userId, mistralConvId, message)
    }

    suspend fun createObjectivesForUser(
        userId: Long,
        titles: List<String>
    ): Result<List<ObjectiveDto>> = runCatching {
        titles.map { title ->
            api.createObjective(
                ObjectiveDto(
                    userId = userId,
                    title = title,
                    endAt = "2026-12-31T23:59:59Z",
                    frequency = 1
                )
            )
        }
    }
}
