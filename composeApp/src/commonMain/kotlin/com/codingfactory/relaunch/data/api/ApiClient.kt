package com.codingfactory.relaunch.data.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiClient {
    // Production backend on Render.
    // For local dev, override at startup: ApiClient.baseUrl = "http://10.0.2.2:8080"
    // (10.0.2.2 = host machine's localhost as seen from the Android emulator).
    var baseUrl: String = "https://relaunch-back.onrender.com"
    // var baseUrl: String = "http://10.0.2.2:8080"

    val httpClient by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                })
            }
        }
    }

    val apiService by lazy { ApiService(httpClient, baseUrl) }
}
