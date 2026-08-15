package com.samir.core
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

fun Long.toLocalDate(): LocalDate {
    return Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
}

fun String.toLocalDate(): LocalDate {
    return toLong().toLocalDate()
}

fun LocalDate.format(): String {
    val dayStr = day.toString().padStart(2, '0')
    val monthStr = month.number.toString().padStart(2, '0')
    return "$dayStr/$monthStr/$year"
}

private const val MILLIS_PER_DAY = 24 * 60 * 60 * 1000L

fun calculateTotalDays(
    checkIn: Long,
    checkOut: Long
): Int {
    return ((checkOut - checkIn) / MILLIS_PER_DAY)
        .coerceAtLeast(1)
        .toInt()
}