package com.samir.hotelmanagement

import com.samir.hotelmanagement.database.BookingRepository
import com.samir.hotelmanagement.database.DatabaseFactory
import com.samir.hotelmanagement.database.HotelRepository
import com.samir.hotelmanagement.database.UserRepository
import com.samir.hotelmanagement.routes.authRouting
import com.samir.hotelmanagement.routes.bookingRouting
import com.samir.hotelmanagement.routes.hotelRouting
import com.samir.hotelmanagement.service.AuthService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = SERVER_HOST, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // Initialize Database
    DatabaseFactory.init()

    // Install Content Negotiation
    install(ContentNegotiation) {
        json()
    }

    val userRepository = UserRepository()
    val authService = AuthService(userRepository)
    val hotelRepository = HotelRepository()
    val bookingRepository = BookingRepository()

    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}...Server is Running now...")
        }
        
        // Register Auth Routes
        authRouting(authService)
        // Register Hotel Routes
        hotelRouting(hotelRepository)
        // Register Booking Routes
        bookingRouting(bookingRepository)
    }
}
