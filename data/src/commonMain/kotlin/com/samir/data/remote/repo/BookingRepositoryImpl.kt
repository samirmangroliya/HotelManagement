package com.samir.data.remote.repo

import com.samir.core.BaseResponse
import com.samir.core.Booking
import com.samir.domain.repo.BookingRepository
import com.samir.network.api.ApiService

class BookingRepositoryImpl(private val apiService: ApiService): BookingRepository {

    override suspend fun createBooking(booking: Booking): BaseResponse<Booking> {
        return apiService.createBooking(booking)
    }

    override suspend fun getBookingsByUser(userId: Int): BaseResponse<List<Booking>> {
        return apiService.getBookingsForUser(userId)
    }

    override suspend fun getBookings(hotelId: Int): BaseResponse<List<Booking>> {
        return apiService.getBookingsForHotel(hotelId)
    }
}