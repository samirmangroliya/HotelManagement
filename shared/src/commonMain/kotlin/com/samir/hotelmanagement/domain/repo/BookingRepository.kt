package com.samir.hotelmanagement.domain.repo

import com.samir.network.models.BaseResponse
import com.samir.network.models.Booking

interface BookingRepository {
   suspend fun createBooking(booking: Booking): BaseResponse<Booking>

   suspend fun getBookingsByUser(userId: Int): BaseResponse<List<Booking>>

   suspend fun getBookings(hotelId: Int): BaseResponse<List<Booking>>
}