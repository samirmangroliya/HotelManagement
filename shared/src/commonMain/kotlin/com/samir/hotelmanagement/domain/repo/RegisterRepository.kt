package com.samir.hotelmanagement.domain.repo

import com.samir.network.models.BaseResponse
import com.samir.network.models.User

interface RegisterRepository {
   suspend fun register(firstName: String, lastName: String, email: String, phone: String, password: String): BaseResponse<User>
}
