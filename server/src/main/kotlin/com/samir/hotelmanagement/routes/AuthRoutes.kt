package com.samir.hotelmanagement.routes

import com.samir.hotelmanagement.models.LoginRequest
import com.samir.hotelmanagement.models.RegisterRequest
import com.samir.hotelmanagement.service.AuthService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.authRouting(authService: AuthService) {
    post("/register") {
        val request = call.receive<RegisterRequest>()
        val response = authService.register(request)
        if (response.success) {
            call.respond(HttpStatusCode.OK, response)
        } else {
            call.respond(HttpStatusCode.BadRequest, response)
        }
    }

    post("/login") {
        val request = call.receive<LoginRequest>()
        val response = authService.login(request)
        if (response.success) {
            call.respond(HttpStatusCode.OK, response)
        } else {
            call.respond(HttpStatusCode.Unauthorized, response)
        }
    }
}
