package com.samir.domain.usercase

import com.samir.core.BaseResponse
import com.samir.core.Room
import com.samir.domain.repo.HotelRepository

class GetRoomsUseCase(
    private val repository: HotelRepository
) {
    suspend operator fun invoke(hotelId: Int): BaseResponse<List<Room>> {
        return repository.getRooms(hotelId)
    }
}
