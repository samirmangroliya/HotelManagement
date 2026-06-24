package com.samir.hotelmanagement.database

import com.samir.core.Booking
import org.jetbrains.exposed.sql.*
class BookingRepository {
    private fun resultRowToBooking(row: ResultRow) = Booking(
        id = row[Bookings.id],
        userId = row[Bookings.userId],
        roomId = row[Bookings.roomId],
        hotelId= row[Bookings.hotelId],
        checkInDate = row[Bookings.checkInDate],
        checkOutDate = row[Bookings.checkOutDate],
        totalPrice = row[Bookings.totalPrice],
        status = row[Bookings.status],
        totalDay = row[Bookings.totalDays]
    )

    suspend fun createBooking(
        userId: Int,
        roomId: Int,
        hotelId: Int,
        checkInDate: String,
        checkOutDate: String,
        totalPrice: Double,
        totalDays: Int
    ): BookingResult = DatabaseFactory.dbQuery {
        // Check for overlapping bookings
        val overlapping = Bookings.selectAll().where {
            (Bookings.roomId eq roomId) and
            (Bookings.checkInDate less checkOutDate) and
            (Bookings.checkOutDate greater checkInDate) and
            (Bookings.status eq "CONFIRMED")
        }.firstOrNull()

        if (overlapping != null) {
            return@dbQuery BookingResult.Conflict(resultRowToBooking(overlapping))
        }

        val insertStatement = Bookings.insert {
            it[Bookings.userId] = userId
            it[Bookings.roomId] = roomId
            it[Bookings.hotelId] = hotelId
            it[Bookings.checkInDate] = checkInDate
            it[Bookings.checkOutDate] = checkOutDate
            it[Bookings.totalPrice] = totalPrice
            it[Bookings.totalDays] = totalDays
            it[Bookings.status] = "CONFIRMED"
        }

        // Also mark the room as unavailable (Global status, though date check is more precise)
        Rooms.update({ Rooms.id eq roomId }) {
            it[Rooms.isAvailable] = false
        }

        val newBooking = insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToBooking)
        if (newBooking != null) {
            BookingResult.Success(newBooking)
        } else {
            BookingResult.Error("Failed to create booking")
        }
    }

    suspend fun getBookingsByUserId(userId: Int): List<Booking> = DatabaseFactory.dbQuery {
        Bookings.selectAll().where { Bookings.userId eq userId }
            .orderBy(Bookings.id to SortOrder.DESC)
            .map(::resultRowToBooking)
    }

    suspend fun getBookingsByHotelId(hotelId: Int): List<Booking> = DatabaseFactory.dbQuery {
        (Bookings innerJoin Rooms)
            .selectAll().where { Rooms.hotelId eq hotelId }
            .orderBy(Bookings.id to SortOrder.DESC)
            .map(::resultRowToBooking)
    }
}
