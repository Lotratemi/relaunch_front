package com.codingfactory.relaunch.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
data class UserDto(
    val id: Long? = null,
    @SerialName("created_at") val createdAt: String? = null,
    val name: String,
    val mail: String,
    val age: Short
)

@Serializable
data class ConversationDto(
    val id: Long? = null,
    @SerialName("user_id") val userId: Long,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("objective_set") val objectiveSet: Boolean = false
)

@Serializable
data class CoachConversationDto(
    val id: Long? = null,
    @SerialName("user_id") val userId: Long,
    @SerialName("mistral_conv_id") val mistralConvId: String
)

@Serializable
data class MessageDto(
    val id: Long? = null,
    @SerialName("conversation_id") val conversationId: Long,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("is_user") val isUser: Boolean,
    val content: String
)

@Serializable
data class ObjectiveDto(
    val id: Long? = null,
    @SerialName("user_id") val userId: Long? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("end_at") val endAt: String,
    val frequency: Long,
    val title: String,
    val description: String? = null
)

@Serializable
data class CoachReplyDto(
    val response: String,
    @SerialName("objectives_creation_trigger") val objectivesCreationTrigger: Boolean,
    val objectives: List<String> = emptyList()
)

@Serializable
data class StreakDto(
    val id: Long? = null,
    @SerialName("user_id") val userId: Long,
    val data: JsonArray,
    @SerialName("started_at") val startedAt: String? = null
)
