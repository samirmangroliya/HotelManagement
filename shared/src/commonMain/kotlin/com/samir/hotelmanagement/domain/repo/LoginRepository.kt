package com.samir.hotelmanagement.domain.repo

import com.samir.network.models.BaseResponse
import com.samir.network.models.User

interface LoginRepository {
   suspend fun login(email: String, password: String): BaseResponse<User>
}