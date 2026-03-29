package com.samir.domain.repo

import com.samir.core.BaseResponse
import com.samir.core.User

interface LoginRepository {
   suspend fun login(email: String, password: String): BaseResponse<User>
}