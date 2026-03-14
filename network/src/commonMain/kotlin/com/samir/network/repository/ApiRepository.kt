package com.samir.network.repository

import com.samir.hotelmanagement.models.*
import com.samir.network.api.ApiService

class ApiRepository(private val apiService: ApiService) {

    suspend fun register(request: RegisterRequest): BaseResponse<User> {
        return apiService.register(request)
    }

    suspend fun login(request: LoginRequest): BaseResponse<User> {
        return apiService.login(request)
    }

    suspend fun getHotels(): BaseResponse<List<Hotel>> {
        return apiService.getHotels()
    }

    suspend fun getRooms(hotelId: Int): BaseResponse<List<Room>> {
        return apiService.getRooms(hotelId)
    }

    suspend fun createBooking(booking: Booking): BaseResponse<Booking> {
        return apiService.createBooking(booking)
    }

    suspend fun getBookingsByUser(userId: Int): BaseResponse<List<Booking>> {
        return apiService.getBookingsForUser(userId)
    }

    suspend fun getBookings(hotelId: Int): BaseResponse<List<Booking>> {
        return apiService.getBookingsForHotel(hotelId)
    }
}
