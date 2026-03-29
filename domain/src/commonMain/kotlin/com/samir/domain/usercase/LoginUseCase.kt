package com.samir.domain.usercase

import com.samir.core.BaseResponse
import com.samir.core.User
import com.samir.domain.repo.LoginRepository

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
