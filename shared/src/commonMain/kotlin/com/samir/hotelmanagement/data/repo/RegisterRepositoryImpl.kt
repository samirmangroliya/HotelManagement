package com.samir.hotelmanagement.data.repo

import com.samir.network.models.BaseResponse
import com.samir.network.models.RegisterRequest
import com.samir.network.models.User
import com.samir.hotelmanagement.domain.repo.RegisterRepository
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
