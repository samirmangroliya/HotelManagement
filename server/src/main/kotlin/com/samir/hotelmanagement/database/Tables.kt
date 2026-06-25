package com.samir.hotelmanagement.database

import org.jetbrains.exposed.v1.core.Table

object Users : Table("users") {

    val id = integer("id").autoIncrement()

    val firstName = varchar("firstname", 128)

    val lastName = varchar("lastname", 128)

    val email = varchar("email", 128)
        .uniqueIndex()

    val password = varchar("password", 128)

    val phone = varchar("phone", 10)
        .uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}

object Hotels : Table("hotels") {

    val id = integer("id").autoIncrement()

    val name = varchar("name", 255)

    val location = varchar("location", 255)

    val description = text("description")

    val imageUrl = varchar("image_url", 512)
        .nullable()

    override val primaryKey = PrimaryKey(id)
}

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