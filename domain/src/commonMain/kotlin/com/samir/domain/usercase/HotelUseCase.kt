package com.samir.domain.usercase

import com.samir.core.BaseResponse
import com.samir.core.Hotel
import com.samir.domain.repo.HotelRepository

class HotelUseCase(
    private val repository: HotelRepository
) {
    suspend operator fun invoke(): BaseResponse<List<Hotel>> {
        return repository.getHotels()
    }
}
