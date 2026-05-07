package com.codingfactory.relaunch.data.repository

import com.codingfactory.relaunch.data.api.ApiService
import com.codingfactory.relaunch.data.model.ProfilingRequestDto
import com.codingfactory.relaunch.data.model.ProfilingResponseDto

class ProfilingRepository(private val api: ApiService) {

    suspend fun analyzeProfiling(
        userId: Long,
        answers: Map<Int, String>
    ): Result<ProfilingResponseDto> = runCatching {
        api.analyzeProfiling(
            ProfilingRequestDto(userId = userId, answers = answers)
        )
    }

    suspend fun getLatestProfile(userId: Long): Result<ProfilingResponseDto> = runCatching {
        api.getLatestProfile(userId)
    }
}