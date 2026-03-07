package com.samir.hotelmanagement.database

import com.samir.hotelmanagement.models.User
import org.jetbrains.exposed.sql.*
class UserRepository {
    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        username = row[Users.username],
        email = row[Users.email]
    )

    suspend fun findUserByUsername(username: String): User? = DatabaseFactory.dbQuery {
        Users.selectAll().where { Users.username eq username }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    suspend fun findUserByEmail(email: String): User? = DatabaseFactory.dbQuery {
        Users.selectAll().where { Users.email eq email }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    suspend fun getPasswordHash(username: String): String? = DatabaseFactory.dbQuery {
        Users.selectAll().where { Users.username eq username }
            .map { it[Users.password] }
            .singleOrNull()
    }

    suspend fun createUser(username: String, email: String, passwordHash: String): User? = DatabaseFactory.dbQuery {
        val insertStatement = Users.insert {
            it[Users.username] = username
            it[Users.email] = email
            it[Users.password] = passwordHash
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }
}
