package com.samir.hotelmanagement.database

import com.samir.hotelmanagement.models.Booking
import org.jetbrains.exposed.sql.*

class BookingRepository {
    private fun resultRowToBooking(row: ResultRow) = Booking(
        id = row[Bookings.id],
        userId = row[Bookings.userId],
        roomId = row[Bookings.roomId],
        checkInDate = row[Bookings.checkInDate],
        checkOutDate = row[Bookings.checkOutDate],
        totalPrice = row[Bookings.totalPrice],
        status = row[Bookings.status]
    )

    suspend fun createBooking(
        userId: Int,
        roomId: Int,
        checkInDate: String,
        checkOutDate: String,
        totalPrice: Double
    ): Booking? = DatabaseFactory.dbQuery {
        val insertStatement = Bookings.insert {
            it[Bookings.userId] = userId
            it[Bookings.roomId] = roomId
            it[Bookings.checkInDate] = checkInDate
            it[Bookings.checkOutDate] = checkOutDate
            it[Bookings.totalPrice] = totalPrice
            it[Bookings.status] = "CONFIRMED"
        }

        // Also mark the room as unavailable
        Rooms.update({ Rooms.id eq roomId }) {
            it[Rooms.isAvailable] = false
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToBooking)
    }

    suspend fun getBookingsByUserId(userId: Int): List<Booking> = DatabaseFactory.dbQuery {
        Bookings.selectAll().where { Bookings.userId eq userId }
            .map(::resultRowToBooking)
    }

    suspend fun getBookingsByHotelId(hotelId: Int): List<Booking> = DatabaseFactory.dbQuery {
        (Bookings innerJoin Rooms)
            .selectAll().where { Rooms.hotelId eq hotelId }
            .map(::resultRowToBooking)
    }
}
