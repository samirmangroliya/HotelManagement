package com.samir.hotelmanagement.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.samir.hotelmanagement.database.UserRepository
import com.samir.hotelmanagement.models.BaseResponse
import com.samir.hotelmanagement.models.LoginRequest
import com.samir.hotelmanagement.models.RegisterRequest
import com.samir.hotelmanagement.models.User

class AuthService(private val userRepository: UserRepository) {

    suspend fun register(request: RegisterRequest): BaseResponse<User> {
        if (userRepository.findUserByUsername(request.username) != null) {
            return BaseResponse(success = false, message = "Username already exists")
        }
        if (userRepository.findUserByEmail(request.email) != null) {
            return BaseResponse(success = false, message = "Email already exists")
        }

        val passwordHash = BCrypt.withDefaults().hashToString(12, request.password.toCharArray())
        val user = userRepository.createUser(request.username, request.email, passwordHash)

        return if (user != null) {
            BaseResponse(success = true, message = "Registration successful", data = user)
        } else {
            BaseResponse(success = false, message = "Registration failed")
        }
    }

    suspend fun login(request: LoginRequest): BaseResponse<User> {
        val user = userRepository.findUserByUsername(request.username)
            ?: return BaseResponse(success = false, message = "Invalid username or password")

        val passwordHash = userRepository.getPasswordHash(request.username)
            ?: return BaseResponse(success = false, message = "Invalid username or password")

        val result = BCrypt.verifyer().verify(request.password.toCharArray(), passwordHash)
        return if (result.verified) {
            BaseResponse(success = true, message = "Login successful", data = user)
        } else {
            BaseResponse(success = false, message = "Invalid username or password")
        }
    }
}
