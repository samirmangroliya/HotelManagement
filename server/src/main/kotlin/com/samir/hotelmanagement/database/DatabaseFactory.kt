package com.samir.hotelmanagement.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        Database.connect(hikari())
        transaction {
            SchemaUtils.create(Users, Hotels, Rooms, Bookings)
           // seedData()
        }
    }

    private fun seedData() {
        if (Hotels.selectAll().empty()) {
            val hiltonId = Hotels.insert {
                it[name] = "Hilton Hotel"
                it[location] = "New York, USA"
                it[description] = "Luxury stay in the heart of NYC."
                it[imageUrl] = "https://images.unsplash.com/photo-1551882547-ff43c69e5cfd"
            } get Hotels.id

            val marriottId = Hotels.insert {
                it[name] = "Marriott Bonvoy"
                it[location] = "Paris, France"
                it[description] = "Classic elegance near the Eiffel Tower."
                it[imageUrl] = "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4"
            } get Hotels.id

            // Add some rooms for Hilton
            Rooms.insert {
                it[hotelId] = hiltonId
                it[roomNumber] = "101"
                it[type] = "Deluxe"
                it[pricePerNight] = 250.0
            }
            Rooms.insert {
                it[hotelId] = hiltonId
                it[roomNumber] = "102"
                it[type] = "Suite"
                it[pricePerNight] = 450.0
            }

            // Add some rooms for Marriott
            Rooms.insert {
                it[hotelId] = marriottId
                it[roomNumber] = "201"
                it[type] = "Standard"
                it[pricePerNight] = 180.0
            }
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        // In a real app, use environment variables for these
        config.jdbcUrl = "jdbc:postgresql://localhost:5432/hotel_db"
        config.username = "postgres"
        config.password = "password"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val firstName = varchar("firstname", 128)
    val lastName = varchar("lastname", 128)
    val email = varchar("email", 128).uniqueIndex()
    val password = varchar("password", 128)
    val phone = varchar("phone", 10).uniqueIndex()
    override val primaryKey = PrimaryKey(id)
}

object Hotels : Table("hotels") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val location = varchar("location", 255)
    val description = text("description")
    val imageUrl = varchar("image_url", 512).nullable()

    override val primaryKey = PrimaryKey(id)
}

object Rooms : Table("rooms") {
    val id = integer("id").autoIncrement()
    val hotelId = integer("hotel_id").references(Hotels.id)
    val roomNumber = varchar("room_number", 50)
    val type = varchar("type", 50)
    val pricePerNight = double("price_per_night")
    val isAvailable = bool("is_available").default(true)

    override val primaryKey = PrimaryKey(id)
}

object Bookings : Table("bookings") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val roomId = integer("room_id").references(Rooms.id)
    val checkInDate = varchar("check_in_date", 50) // Simplified as String for now
    val checkOutDate = varchar("check_out_date", 50)
    val totalPrice = double("total_price")
    val status = varchar("status", 50).default("PENDING")

    override val primaryKey = PrimaryKey(id)
}
