package com.samir.hotelmanagement

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform