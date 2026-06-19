package com.samir.hotelmanagement.database

import com.samir.core.Booking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class BookingRepository {
    private fun resultRowToBooking(row: ResultRow) = Booking(
        id = row[Bookings.id],
        userId = row[Bookings.userId],
        roomId = row[Bookings.roomId],
        checkInDate = row[Bookings.checkInDate],
        checkOutDate = row[Bookings.checkOutDate],
        totalPrice = row[Bookings.totalPrice],
        status = row[Bookings.status],
        totalDay = row[Bookings.totalDays]
    )

    suspend fun createBooking(
        userId: Int,
        roomId: Int,
        checkInDate: String,
        checkOutDate: String,
        totalPrice: Double,
        totalDays: Int
    ): Booking? = DatabaseFactory.dbQuery {
        val insertStatement = Bookings.insert {
            it[Bookings.userId] = userId
            it[Bookings.roomId] = roomId
            it[Bookings.checkInDate] = checkInDate
            it[Bookings.checkOutDate] = checkOutDate
            it[Bookings.totalPrice] = totalPrice
            it[Bookings.totalDays] = totalDays
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
