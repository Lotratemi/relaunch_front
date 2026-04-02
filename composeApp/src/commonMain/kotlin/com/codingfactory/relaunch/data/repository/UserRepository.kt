package com.codingfactory.relaunch.data.repository

import com.codingfactory.relaunch.data.api.ApiService
import com.codingfactory.relaunch.data.model.UserDto

class UserRepository(private val api: ApiService) {

    suspend fun createUser(name: String, mail: String, age: Short): Result<UserDto> = runCatching {
        api.createUser(UserDto(name = name, mail = mail, age = age))
    }

    suspend fun getUser(id: Long): Result<UserDto> = runCatching {
        api.getUser(id)
    }
}
