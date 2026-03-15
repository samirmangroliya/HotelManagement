package com.samir.hotelmanagement.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.samir.hotelmanagement.database.UserRepository
import com.samir.network.models.BaseResponse
import com.samir.network.models.LoginRequest
import com.samir.network.models.RegisterRequest
import com.samir.network.models.User

class AuthService(private val userRepository: UserRepository) {

    suspend fun register(request: RegisterRequest): BaseResponse<User> {

        if (request.firstName.isBlank()) {
            return BaseResponse(success = false, message = "First name cannot be blank")
        }
        if (request.lastName.isBlank()) {
            return BaseResponse(success = false, message = "Last name cannot be blank")
        }
        if (request.firstName == request.lastName) {
            return BaseResponse(success = false, message = "First name and last name cannot be the same")
        }

        if (request.email.isBlank()) {
            return BaseResponse(success = false, message = "email cannot be blank")
        }
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$"
        if (!Regex(emailRegex).matches(request.email)) {
            return BaseResponse(success = false, message = "Invalid email format")
        }
        if (request.phone.length != 10) {
            return BaseResponse(success = false, message = "Phone number should be 10 chars long")
        }
        if (request.password.length < 6) {
            return BaseResponse(success = false, message = "Password should be at least 6 chars long")
        }

        if (userRepository.findUserByEmail(request.email) != null) {
            return BaseResponse(success = false, message = "Email already exists")
        }

        if (userRepository.findUserByEmail(request.phone) != null) {
            return BaseResponse(success = false, message = "Phone Number already exists")
        }

        val passwordHash = BCrypt.withDefaults().hashToString(12, request.password.toCharArray())
        val user = userRepository.createUser(request.firstName, request.lastName, request.phone, request.email, passwordHash)

        return if (user != null) {
            BaseResponse(success = true, message = "Registration successful, your id is: {${user.id}}", data = user)
        } else {
            BaseResponse(success = false, message = "Registration failed")
        }
    }

    suspend fun login(request: LoginRequest): BaseResponse<User> {
        if (request.email.isBlank()) {
            return BaseResponse(success = false, message = "email cannot be blank")
        }
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$"
        if (!Regex(emailRegex).matches(request.email)) {
            return BaseResponse(success = false, message = "Invalid email format")
        }
        if (request.password.length < 6) {
            return BaseResponse(success = false, message = "Password should be at least 6 chars long")
        }

        val user = userRepository.findUserByEmail(request.email)
            ?: return BaseResponse(success = false, message = "Invalid username or password")

        val passwordHash = userRepository.getPasswordHash(request.email)
            ?: return BaseResponse(success = false, message = "Invalid username or password")

        val result = BCrypt.verifyer().verify(request.password.toCharArray(), passwordHash)
        return if (result.verified) {
            BaseResponse(success = true, message = "Login successful", data = user)
        } else {
            BaseResponse(success = false, message = "Invalid username or password")
        }
    }
}
