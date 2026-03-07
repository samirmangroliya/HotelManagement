package com.samir.hotelmanagement.database

import com.samir.hotelmanagement.models.User
import org.jetbrains.exposed.sql.*
import kotlin.Int

class UserRepository {
    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        email = row[Users.email],
        firstName = row[Users.firstName],
        lastName = row[Users.lastName],
        phone = row[Users.phone]
    )

    suspend fun findUserByEmail(email: String): User? = DatabaseFactory.dbQuery {
        Users.selectAll().where { Users.email eq email }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    suspend fun getPasswordHash(email: String): String? = DatabaseFactory.dbQuery {
        Users.selectAll().where { Users.email eq email }
            .map { it[Users.password] }
            .singleOrNull()
    }

    suspend fun createUser(firstName: String, lastName: String, phone: String, email: String, passwordHash: String): User? = DatabaseFactory.dbQuery {
        val insertStatement = Users.insert {
            it[Users.firstName] = firstName
            it[Users.lastName] = lastName
            it[Users.phone] = phone
            it[Users.email] = email
            it[Users.password] = passwordHash
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }
}
