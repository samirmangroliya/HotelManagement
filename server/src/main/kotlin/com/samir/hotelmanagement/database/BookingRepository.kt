package com.samir.hotelmanagement.database

import com.samir.core.Booking
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

class BookingRepository {

    private fun resultRowToBooking(row: ResultRow): Booking =
        Booking(
            id = row[Bookings.id],
            userId = row[Bookings.userId],
            roomId = row[Bookings.roomId],
            hotelId = row[Bookings.hotelId],
            checkInDate = row[Bookings.checkInDate],
            checkOutDate = row[Bookings.checkOutDate],
            totalPrice = row[Bookings.totalPrice],
            totalDay = row[Bookings.totalDays],
            status = row[Bookings.status]
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

        val existingBooking = Bookings
            .selectAll()
            .where {
                (Bookings.roomId eq roomId) and
                        (Bookings.status eq "CONFIRMED")
            }
            .firstOrNull()

        if (existingBooking != null) {
            return@dbQuery BookingResult.Conflict(
                resultRowToBooking(existingBooking)
            )
        }

        Bookings.insert {
            it[Bookings.userId] = userId
            it[Bookings.roomId] = roomId
            it[Bookings.hotelId] = hotelId
            it[Bookings.checkInDate] = checkInDate
            it[Bookings.checkOutDate] = checkOutDate
            it[Bookings.totalPrice] = totalPrice
            it[Bookings.totalDays] = totalDays
            it[Bookings.status] = "CONFIRMED"
        }

        val booking = Bookings
            .selectAll()
            .where {
                (Bookings.userId eq userId) and
                        (Bookings.roomId eq roomId)
            }
            .orderBy(Bookings.id to SortOrder.DESC)
            .firstOrNull()
            ?.let(::resultRowToBooking)

        if (booking != null) {
            BookingResult.Success(booking)
        } else {
            BookingResult.Error("Failed to create booking")
        }
    }

    suspend fun getBookingsByUserId(
        userId: Int
    ): List<Booking> = DatabaseFactory.dbQuery {

        Bookings
            .selectAll()
            .where {
                Bookings.userId eq userId
            }
            .orderBy(Bookings.id to SortOrder.DESC)
            .map(::resultRowToBooking)
    }

    suspend fun getBookingsByHotelId(
        hotelId: Int
    ): List<Booking> = DatabaseFactory.dbQuery {

        Bookings
            .selectAll()
            .where {
                Bookings.hotelId eq hotelId
            }
            .orderBy(Bookings.id to SortOrder.DESC)
            .map(::resultRowToBooking)
    }
}