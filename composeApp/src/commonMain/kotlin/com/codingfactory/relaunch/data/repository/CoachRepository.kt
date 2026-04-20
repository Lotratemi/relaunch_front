package com.codingfactory.relaunch.data.repository

import com.codingfactory.relaunch.data.api.ApiService
import com.codingfactory.relaunch.data.model.CoachConversationDto
import com.codingfactory.relaunch.data.model.ConversationDto
import com.codingfactory.relaunch.data.model.MessageDto
import com.codingfactory.relaunch.data.model.ObjectiveDto

class CoachRepository(private val api: ApiService) {

    suspend fun createConversation(userId: Long): Result<ConversationDto> = runCatching {
        api.createConversation(ConversationDto(userId = userId))
    }

    suspend fun startCoachConversation(userId: Long): Result<CoachConversationDto> = runCatching {
        api.startCoachConversation(userId)
    }

    suspend fun sendCoachMessage(
        userId: Long,
        mistralConvId: String,
        message: String
    ): Result<String> = runCatching {
        api.sendCoachMessage(userId, mistralConvId, message)
    }

    suspend fun saveMessage(
        conversationId: Long,
        content: String,
        isUser: Boolean
    ): Result<MessageDto> = runCatching {
        api.createMessage(
            MessageDto(
                conversationId = conversationId,
                isUser = isUser,
                content = content
            )
        )
    }

    suspend fun markConversationDone(
        conversationId: Long,
        userId: Long
    ): Result<ConversationDto> = runCatching {
        api.updateConversation(
            conversationId,
            ConversationDto(userId = userId, objectiveSet = true)
        )
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
