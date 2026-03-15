package com.samir.hotelmanagement.data.repo

import com.samir.network.models.BaseResponse
import com.samir.network.models.Booking
import com.samir.hotelmanagement.domain.repo.BookingRepository
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