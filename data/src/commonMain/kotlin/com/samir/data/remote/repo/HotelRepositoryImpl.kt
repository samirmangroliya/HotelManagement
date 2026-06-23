package com.samir.data.remote.repo

import com.samir.core.BaseResponse
import com.samir.core.Hotel
import com.samir.core.Room
import com.samir.domain.repo.HotelRepository
import com.samir.network.api.ApiService

class HotelRepositoryImpl(private val apiService: ApiService): HotelRepository {
    override suspend fun getHotels(): BaseResponse<List<Hotel>> {
         return  apiService.getHotels()
    }

    override suspend fun getRooms(hotelId: Int): BaseResponse<List<Room>> {
        return apiService.getRooms(hotelId)
    }

    override suspend fun getHotelById(hotelId: Int): BaseResponse<Hotel> {
        return apiService.getHotelById(hotelId)
    }
}