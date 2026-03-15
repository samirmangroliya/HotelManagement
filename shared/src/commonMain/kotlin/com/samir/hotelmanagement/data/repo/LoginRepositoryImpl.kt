package com.samir.hotelmanagement.data.repo

import com.samir.network.models.BaseResponse
import com.samir.network.models.LoginRequest
import com.samir.network.models.User
import com.samir.hotelmanagement.domain.repo.LoginRepository
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