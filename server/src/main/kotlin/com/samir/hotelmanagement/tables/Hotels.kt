package com.samir.hotelmanagement.tables

import org.jetbrains.exposed.v1.core.Table

object Hotels : Table("hotels") {

    val id = integer("id").autoIncrement()

    val name = varchar("name", 255)

    val location = varchar("location", 255)

    val description = text("description")

    val imageUrl = varchar("image_url", 512)
        .nullable()

    override val primaryKey = PrimaryKey(id)
}