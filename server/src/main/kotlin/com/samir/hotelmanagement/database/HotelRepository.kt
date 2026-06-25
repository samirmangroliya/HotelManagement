package com.samir.hotelmanagement.database

import com.samir.core.Hotel
import com.samir.core.Room
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

class HotelRepository {
    private fun resultRowToHotel(row: ResultRow) = Hotel(
        id = row[Hotels.id],
        name = row[Hotels.name],
        location = row[Hotels.location],
        description = row[Hotels.description],
        imageUrl = row[Hotels.imageUrl]
    )

    private fun resultRowToRoom(row: ResultRow) = Room(
        id = row[Rooms.id],
        hotelId = row[Rooms.hotelId],
        roomNumber = row[Rooms.roomNumber],
        type = row[Rooms.type],
        pricePerNight = row[Rooms.pricePerNight]
    )

    suspend fun getAllHotels(): List<Hotel> = DatabaseFactory.dbQuery {
        Hotels.selectAll().orderBy(Hotels.id to SortOrder.ASC).map(::resultRowToHotel)
    }

    suspend fun getHotelById(id: Int): Hotel? = DatabaseFactory.dbQuery {
        Hotels.selectAll().where { Hotels.id eq id }
            .map(::resultRowToHotel)
            .singleOrNull()
    }

    suspend fun getRoomsByHotelId(hotelId: Int): List<Room> = DatabaseFactory.dbQuery {
        Rooms
            .selectAll()
            .where { Rooms.hotelId eq hotelId }
            .orderBy(Rooms.roomNumber to SortOrder.DESC)
            .map(::resultRowToRoom)
            .groupBy { it.type }
            .map { (_, rooms) ->
                rooms.first()
            }
    }

    suspend fun createHotel(name: String, location: String, description: String, imageUrl: String?): Hotel? = DatabaseFactory.dbQuery {
        val insertStatement = Hotels.insert {
            it[Hotels.name] = name
            it[Hotels.location] = location
            it[Hotels.description] = description
            it[Hotels.imageUrl] = imageUrl
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToHotel)
    }

    suspend fun addRoom(hotelId: Int, roomNumber: String, type: String, pricePerNight: Double): Room? = DatabaseFactory.dbQuery {
        val insertStatement = Rooms.insert {
            it[Rooms.hotelId] = hotelId
            it[Rooms.roomNumber] = roomNumber
            it[Rooms.type] = type
            it[Rooms.pricePerNight] = pricePerNight
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToRoom)
    }
}
