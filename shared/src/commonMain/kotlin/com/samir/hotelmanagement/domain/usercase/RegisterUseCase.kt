package com.samir.hotelmanagement.domain.usercase

import com.samir.network.models.BaseResponse
import com.samir.network.models.RegisterRequest
import com.samir.network.models.User
import com.samir.hotelmanagement.domain.repo.RegisterRepository

class RegisterUseCase(
    private val repository: RegisterRepository
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        password: String
    ): BaseResponse<User> {
        return repository.register(
                email = email,
                password = password,
                firstName = firstName,
                lastName = lastName,
                phone = phone
        )
    }
}
