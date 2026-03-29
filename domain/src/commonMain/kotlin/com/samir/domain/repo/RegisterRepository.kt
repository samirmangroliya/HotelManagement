package com.samir.domain.repo

import com.samir.core.BaseResponse
import com.samir.core.User

interface RegisterRepository {
   suspend fun register(firstName: String, lastName: String, email: String, phone: String, password: String): BaseResponse<User>
}
