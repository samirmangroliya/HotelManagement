package com.samir.hotelmanagement.routes

import com.samir.core.BaseResponse
import com.samir.core.Booking
import com.samir.hotelmanagement.database.BookingRepository
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

            val booking = bookingRepository.createBooking(
                userId = bookingRequest.userId,
                roomId = bookingRequest.roomId,
                checkInDate = bookingRequest.checkInDate,
                checkOutDate = bookingRequest.checkOutDate,
                totalPrice = bookingRequest.totalPrice,
                totalDays = bookingRequest.totalDay
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
