package com.samir.hotelmanagement.tables

import org.jetbrains.exposed.v1.core.Table

object Bookings : Table("bookings") {

    val id = integer("id").autoIncrement()

    val userId = integer("user_id")
        .references(Users.id)

    val roomId = integer("room_id")
        .references(Rooms.id)

    val hotelId = integer("hotel_id")
        .references(Hotels.id)

    // Recommended: store epoch millis
    val checkInDate = varchar("check_in_date", 50)

    val checkOutDate = varchar("check_out_date", 50)

    val totalPrice = double("total_price")

    val totalDays = integer("total_days")

    val status = varchar("status", 50)
        .default("Pending")

    override val primaryKey = PrimaryKey(id)
}