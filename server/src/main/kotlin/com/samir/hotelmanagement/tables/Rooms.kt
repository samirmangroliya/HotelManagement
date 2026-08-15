package com.samir.hotelmanagement.tables

import org.jetbrains.exposed.v1.core.Table

object Rooms : Table("rooms") {

    val id = integer("id").autoIncrement()

    val hotelId = integer("hotel_id")
        .references(Hotels.id)

    val roomNumber = varchar("room_number", 50)

    val type = varchar("type", 50)

    val pricePerNight = double("price_per_night")

    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex(hotelId, roomNumber)
    }
}