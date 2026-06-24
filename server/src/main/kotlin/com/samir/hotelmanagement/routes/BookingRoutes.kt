package com.samir.hotelmanagement.routes

import com.samir.core.BaseResponse
import com.samir.core.Booking
import com.samir.core.format
import com.samir.core.toLocalDate
import com.samir.hotelmanagement.database.BookingRepository
import com.samir.hotelmanagement.database.BookingResult
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.bookingRouting(bookingRepository: BookingRepository) {
    route("/bookings") {
        post("/create") {
            val bookingRequest = try {
                call.receive<Booking>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse<Unit>(success = false, message = "Invalid request body:: ${e.message}"))
                return@post
            }

            val result = bookingRepository.createBooking(
                userId = bookingRequest.userId,
                roomId = bookingRequest.roomId,
                hotelId = bookingRequest.hotelId,
                checkInDate = bookingRequest.checkInDate,
                checkOutDate = bookingRequest.checkOutDate,
                totalPrice = bookingRequest.totalPrice,
                totalDays = bookingRequest.totalDay
            )

            when (result) {
                is BookingResult.Success -> {
                    call.respond(HttpStatusCode.Created, BaseResponse(success = true, message = "Booking created successfully", data = result.booking))
                }
                is BookingResult.Conflict -> {
                    val existing = result.existingBooking
                    val checkIn = existing.checkInDate.toLocalDate().format()
                    val checkOut = existing.checkOutDate.toLocalDate().format()
                    
                    call.respond(HttpStatusCode.Conflict, BaseResponse<Unit>(
                        success = false, 
                        message = "Room is already booked from $checkIn to $checkOut. Please try other dates or check for other available rooms."
                    ))
                }
                is BookingResult.Error -> {
                    call.respond(HttpStatusCode.InternalServerError, BaseResponse<Unit>(success = false, message = result.message))
                }
            }
        }

        get("/{userId}") {
            val userId = call.parameters["userId"]?.toIntOrNull()
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse<Unit>(success = false, message = "Invalid Hotel ID"))
                return@get
            }
            val bookings = bookingRepository.getBookingsByUserId(userId)
            call.respond(BaseResponse(success = true, message = "Bookings for user fetched successfully", data = bookings))
        }

        get("/hotel/{hotelId}") {
            val hotelId = call.parameters["hotelId"]?.toIntOrNull()
            if (hotelId == null) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse<Unit>(success = false, message = "Invalid Hotel ID"))
                return@get
            }
            val bookings = bookingRepository.getBookingsByHotelId(hotelId)
            call.respond(BaseResponse(success = true, message = "Bookings for hotel fetched successfully", data = bookings))
        }
    }
}
