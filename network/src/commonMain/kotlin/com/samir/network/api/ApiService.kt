package com.samir.network.api

import com.samir.network.models.BaseResponse
import com.samir.network.models.Booking
import com.samir.network.models.Hotel
import com.samir.network.models.LoginRequest
import com.samir.network.models.RegisterRequest
import com.samir.network.models.Room
import com.samir.network.models.User
import com.samir.network.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class ApiService(private val client: HttpClient) {

    suspend fun register(request: RegisterRequest): BaseResponse<User> {
        return client.post("${Constants.BASE_URL}${Constants.REGISTER_URL}") {
            setBody(request)
        }.body()
    }

    suspend fun login(request: LoginRequest): BaseResponse<User> {
        return client.post("${Constants.BASE_URL}${Constants.LOGIN_URL}") {
            setBody(request)
        }.body()
    }

    suspend fun getHotels(): BaseResponse<List<Hotel>> {
        return client.get("${Constants.BASE_URL}${Constants.HOTELS_URL}").body()
    }

    suspend fun getRooms(hotelId: Int): BaseResponse<List<Room>> {
        return client.get("${Constants.BASE_URL}${Constants.HOTELS_URL}/$hotelId/${Constants.ROOMS_URL}").body()
    }

    suspend fun createBooking(request: Booking): BaseResponse<Booking> {
        return client.post("${Constants.BASE_URL}${Constants.CREATE_BOOKING_URL}") {
            setBody(request)
        }.body()
    }

    suspend fun getBookingsForUser(userId: Int): BaseResponse<List<Booking>> {
        return client.get("${Constants.BASE_URL}${Constants.BOOKINGS_URL}/$userId").body()
    }
    
    suspend fun getBookingsForHotel(hotelId: Int): BaseResponse<List<Booking>> {
        return client.get("${Constants.BASE_URL}${Constants.HOTELS_URL}/${Constants.HOTEL_URL}/$hotelId").body()
    }
}
