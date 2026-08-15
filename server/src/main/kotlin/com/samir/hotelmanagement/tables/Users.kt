package com.samir.hotelmanagement.tables

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