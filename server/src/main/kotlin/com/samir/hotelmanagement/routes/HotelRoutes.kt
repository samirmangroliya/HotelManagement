package com.samir.hotelmanagement.routes

import com.samir.core.BaseResponse
import com.samir.hotelmanagement.database.HotelRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.hotelRouting(hotelRepository: HotelRepository) {
    route("/hotels") {
        get {
            val hotels = hotelRepository.getAllHotels()
            call.respond(
                BaseResponse(
                    success = true,
                    message = "Hotels fetched successfully",
                    data = hotels
                )
            )
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse<Unit>(success = false, message = "Invalid ID"))
                return@get
            }
            val hotel = hotelRepository.getHotelById(id)
            if (hotel != null) {
                call.respond(BaseResponse(success = true, message = "Hotel fetched successfully", data = hotel))
            } else {
                call.respond(HttpStatusCode.NotFound, BaseResponse<Unit>(success = false, message = "Hotel not found"))
            }
        }
    }
}
