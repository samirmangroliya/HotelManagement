package com.samir.hotelmanagement.domain.repo

import com.samir.network.models.BaseResponse
import com.samir.network.models.Hotel
import com.samir.network.models.Room
import com.samir.network.models.User

interface HotelRepository {
   suspend fun getHotels(): BaseResponse<List<Hotel>>

   suspend fun getRooms(hotelId: Int): BaseResponse<List<Room>>
}