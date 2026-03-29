package com.samir.core

import kotlinx.serialization.Serializable

@Serializable
data class Hotel(
    val id: Int,
    val name: String,
    val location: String,
    val description: String,
    val imageUrl: String? = null
)

@Serializable
data class Room(
    val id: Int,
    val hotelId: Int,
    val roomNumber: String,
    val type: String,
    val pricePerNight: Double,
    val isAvailable: Boolean = true
)

@Serializable
data class Booking(
    val id: Int,
    val userId: Int,
    val roomId: Int,
    val checkInDate: String,
    val checkOutDate: String,
    val totalPrice: Double,
    val status: String
)
