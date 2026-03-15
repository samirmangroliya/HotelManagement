package com.samir.hotelmanagement.routes

import com.samir.hotelmanagement.database.HotelRepository
import com.samir.network.models.BaseResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.hotelRouting(hotelRepository: HotelRepository) {
    route("/hotels") {
        get {
            val hotels = hotelRepository.getAllHotels()
            call.respond(BaseResponse(success = true, message = "Hotels fetched successfully", data = hotels))
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

        get("{id}/rooms") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse<Unit>(success = false, message = "Invalid ID"))
                return@get
            }
            val rooms = hotelRepository.getRoomsByHotelId(id)
            call.respond(BaseResponse(success = true, message = "Rooms fetched successfully", data = rooms))
        }
    }
}
