package com.samir.data.remote.repo

import com.samir.core.BaseResponse
import com.samir.core.LoginRequest
import com.samir.core.User
import com.samir.domain.repo.LoginRepository
import com.samir.network.api.ApiService

class LoginRepositoryImpl(private val apiService: ApiService): LoginRepository {
    override suspend fun login(
        email: String,
        password: String
    ): BaseResponse<User> {
        val loginRequest = LoginRequest(email, password)
        return apiService.login(loginRequest)
    }
}