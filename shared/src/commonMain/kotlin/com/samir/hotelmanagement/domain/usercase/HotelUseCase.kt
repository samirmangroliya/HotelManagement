package com.samir.hotelmanagement.domain.usercase

import com.samir.network.models.BaseResponse
import com.samir.network.models.Hotel
import com.samir.hotelmanagement.domain.repo.HotelRepository

class HotelUseCase(
    private val repository: HotelRepository
) {
    suspend operator fun invoke(): BaseResponse<List<Hotel>> {
        return repository.getHotels()
    }
}
