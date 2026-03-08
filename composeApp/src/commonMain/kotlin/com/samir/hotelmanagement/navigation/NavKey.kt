package com.samir.hotelmanagement.navigation

sealed interface NavKey {
    data object Login : NavKey
    data object Register : NavKey
    data object Main: NavKey
}