package com.samir.hotelmanagement.database

import com.samir.core.Booking

sealed class BookingResult {
    data class Success(val booking: Booking) : BookingResult()
    data class Conflict(val existingBooking: Booking) : BookingResult()
    data class Error(val message: String) : BookingResult()
}
