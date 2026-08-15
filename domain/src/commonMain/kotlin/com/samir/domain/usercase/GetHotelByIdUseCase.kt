package com.samir.domain.usercase

import com.samir.core.BaseResponse
import com.samir.core.Hotel
import com.samir.domain.repo.HotelRepository

class GetHotelByIdUseCase(
    private val repository: HotelRepository
) {
    suspend operator fun invoke(hotelId: Int): BaseResponse<Hotel> {
        return repository.getHotelById(hotelId)
    }
}
