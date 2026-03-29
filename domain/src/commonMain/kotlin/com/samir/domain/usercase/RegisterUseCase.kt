package com.samir.domain.usercase

import com.samir.core.BaseResponse
import com.samir.core.User
import com.samir.domain.repo.RegisterRepository


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
