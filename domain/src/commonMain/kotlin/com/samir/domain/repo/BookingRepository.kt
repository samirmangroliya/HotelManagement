package com.samir.domain.repo

import com.samir.core.BaseResponse
import com.samir.core.Booking


interface BookingRepository {
   suspend fun createBooking(booking: Booking): BaseResponse<Booking>

   suspend fun getBookingsByUser(userId: Int): BaseResponse<List<Booking>>

   suspend fun getBookings(hotelId: Int): BaseResponse<List<Booking>>
}