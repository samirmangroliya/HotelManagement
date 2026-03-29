package com.samir.domain.repo

import com.samir.core.BaseResponse
import com.samir.core.Hotel
import com.samir.core.Room


interface HotelRepository {
   suspend fun getHotels(): BaseResponse<List<Hotel>>

   suspend fun getRooms(hotelId: Int): BaseResponse<List<Room>>
}