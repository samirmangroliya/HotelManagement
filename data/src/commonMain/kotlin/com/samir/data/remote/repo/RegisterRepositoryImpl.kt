package com.samir.data.remote.repo

import com.samir.core.BaseResponse
import com.samir.core.RegisterRequest
import com.samir.core.User
import com.samir.domain.repo.RegisterRepository
import com.samir.network.api.ApiService

class RegisterRepositoryImpl(private val apiService: ApiService): RegisterRepository {
    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        password: String
    ): BaseResponse<User> {
         val registerRequest = RegisterRequest(
             firstName = firstName,
             lastName = lastName,
             email = email,
             phone = phone, 
             password = password
         )
         return apiService.register(registerRequest)
    }
}
