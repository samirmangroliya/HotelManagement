package com.samir.network.api

import com.samir.hotelmanagement.models.BaseResponse
import com.samir.hotelmanagement.models.Booking
import com.samir.hotelmanagement.models.Hotel
import com.samir.hotelmanagement.models.LoginRequest
import com.samir.hotelmanagement.models.RegisterRequest
import com.samir.hotelmanagement.models.Room
import com.samir.hotelmanagement.models.User
import com.samir.network.util.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class ApiService(private val client: HttpClient) {

    suspend fun register(request: RegisterRequest): BaseResponse<User> {
        return client.post("${Constants.BASE_URL}register") {
            setBody(request)
        }.body()
    }

    suspend fun login(request: LoginRequest): BaseResponse<User> {
        return client.post("${Constants.BASE_URL}login") {
            setBody(request)
        }.body()
    }

    suspend fun getHotels(): BaseResponse<List<Hotel>> {
        return client.get("${Constants.BASE_URL}hotels").body()
    }

    suspend fun getRooms(hotelId: Int): BaseResponse<List<Room>> {
        return client.get("${Constants.BASE_URL}hotels/$hotelId/rooms").body()
    }

    suspend fun createBooking(request: Booking): BaseResponse<Booking> {
        return client.post("${Constants.BASE_URL}bookings/create") {
            setBody(request)
        }.body()
    }

    suspend fun getBookingsForUser(userId: Int): BaseResponse<List<Booking>> {
        return client.get("${Constants.BASE_URL}bookings/$userId").body()
    }
    
    suspend fun getBookingsForHotel(hotelId: Int): BaseResponse<List<Booking>> {
        return client.get("${Constants.BASE_URL}bookings/hotel/$hotelId").body()
    }
}
