package com.samir.hotelmanagement.data.repo

import com.samir.network.models.BaseResponse
import com.samir.network.models.Hotel
import com.samir.network.models.Room
import com.samir.hotelmanagement.domain.repo.HotelRepository
import com.samir.network.api.ApiService

class HotelRepositoryImpl(private val apiService: ApiService): HotelRepository {
    override suspend fun getHotels(): BaseResponse<List<Hotel>> {
         return  apiService.getHotels()
    }

    override suspend fun getRooms(hotelId: Int): BaseResponse<List<Room>> {
        return apiService.getRooms(hotelId)
    }
}