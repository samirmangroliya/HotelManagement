package com.samir.hotelmanagement.domain.usercase

import com.samir.network.models.BaseResponse
import com.samir.network.models.User
import com.samir.hotelmanagement.domain.repo.LoginRepository

class LoginUseCase(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): BaseResponse<User> {
        return repository.login(email, password)
    }
}
