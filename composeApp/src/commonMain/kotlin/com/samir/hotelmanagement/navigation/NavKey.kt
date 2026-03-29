package com.samir.hotelmanagement.navigation

sealed interface NavKey {
    data object Login : NavKey
    data object Register : NavKey
    data object Main: NavKey

    data object HotelList : NavKey

    data class HotelDetails(val hotelId: Int) : NavKey
}