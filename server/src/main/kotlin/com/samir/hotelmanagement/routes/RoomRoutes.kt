package com.samir.hotelmanagement.routes

import com.samir.core.BaseResponse
import com.samir.hotelmanagement.database.HotelRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.roomRouting(hotelRepository: HotelRepository) {
    route("/hotels") {
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
