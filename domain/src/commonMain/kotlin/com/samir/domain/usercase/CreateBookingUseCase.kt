package com.samir.domain.usercase

import com.samir.core.BaseResponse
import com.samir.core.Booking
import com.samir.domain.repo.BookingRepository

class CreateBookingUseCase(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(booking: Booking): BaseResponse<Booking> {
        return repository.createBooking(booking)
    }
}
