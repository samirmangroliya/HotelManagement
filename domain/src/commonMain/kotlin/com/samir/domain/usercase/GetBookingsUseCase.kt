package com.samir.domain.usercase

import com.samir.core.BaseResponse
import com.samir.core.Booking
import com.samir.domain.repo.BookingRepository

class GetBookingsUseCase(private val repository: BookingRepository) {
    suspend operator fun invoke(userId: Int): BaseResponse<List<Booking>> {
        return repository.getBookingsByUser(userId)
    }
}
