package com.codingfactory.relaunch.data.api

import com.codingfactory.relaunch.data.model.CoachConversationDto
import com.codingfactory.relaunch.data.model.CoachReplyDto
import com.codingfactory.relaunch.data.model.ObjectiveDto
import com.codingfactory.relaunch.data.model.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ApiService(private val client: HttpClient, private val baseUrl: String) {

    // ── Users ──

    suspend fun createUser(user: UserDto): UserDto =
        client.post("$baseUrl/users") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.body()

    suspend fun getUser(id: Long): UserDto =
        client.get("$baseUrl/users/$id").body()

    suspend fun updateUser(id: Long, user: UserDto): UserDto =
        client.put("$baseUrl/users/$id") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.body()

    // ── Objectives ──

    suspend fun createObjective(objective: ObjectiveDto): ObjectiveDto =
        client.post("$baseUrl/objectives") {
            contentType(ContentType.Application.Json)
            setBody(objective)
        }.body()

    suspend fun getObjectivesByUser(userId: Long): List<ObjectiveDto> =
        client.get("$baseUrl/objectives/$userId").body()

    suspend fun updateObjective(userId: Long, objectiveId: Long, objective: ObjectiveDto): ObjectiveDto =
        client.put("$baseUrl/objectives/$userId/$objectiveId") {
            contentType(ContentType.Application.Json)
            setBody(objective)
        }.body()

    suspend fun deleteObjective(userId: Long, objectiveId: Long) {
        client.delete("$baseUrl/objectives/$userId/$objectiveId")
    }

    // ── Coach (Mistral AI) ──

    suspend fun startCoachConversation(userId: Long): CoachConversationDto =
        client.post("$baseUrl/coach/chat/$userId").body()

    suspend fun sendCoachMessage(
        userId: Long,
        mistralConvId: String,
        message: String
    ): CoachReplyDto =
        client.post("$baseUrl/coach/chat/$userId/$mistralConvId") {
            contentType(ContentType.Text.Plain)
            setBody(message)
        }.body()

}
