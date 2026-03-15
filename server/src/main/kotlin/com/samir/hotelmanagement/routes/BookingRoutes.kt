package com.samir.hotelmanagement.routes

import com.samir.hotelmanagement.database.BookingRepository
import com.samir.network.models.BaseResponse
import com.samir.network.models.Booking
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.bookingRouting(bookingRepository: BookingRepository) {
    route("/bookings") {
        post("/create") {
            val bookingRequest = try {
                call.receive<Booking>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse<Unit>(success = false, message = "Invalid request body"))
                return@post
            }

            val booking = bookingRepository.createBooking(
                userId = bookingRequest.userId,
                roomId = bookingRequest.roomId,
                checkInDate = bookingRequest.checkInDate,
                checkOutDate = bookingRequest.checkOutDate,
                totalPrice = bookingRequest.totalPrice
            )

            if (booking != null) {
                call.respond(HttpStatusCode.Created, BaseResponse(success = true, message = "Booking created successfully", data = booking))
            } else {
                call.respond(HttpStatusCode.InternalServerError, BaseResponse<Unit>(success = false, message = "Failed to create booking"))
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
