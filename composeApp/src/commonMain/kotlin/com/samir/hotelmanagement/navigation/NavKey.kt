package com.samir.hotelmanagement.navigation

import com.samir.core.Hotel


sealed interface NavKey {
    data object Login : NavKey
    data object Register : NavKey
    data object Main: NavKey
    data object HotelList: NavKey
    data class HotelDetails(val hotel: Hotel): NavKey
    data class Booking(val hotel: Hotel, val userId: Int): NavKey
}

fun MutableList<NavKey>.goBack() {
    if (size > 1) {
        removeAt(size - 1)
    }
}
