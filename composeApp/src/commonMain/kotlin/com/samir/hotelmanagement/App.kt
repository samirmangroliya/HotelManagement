package com.samir.hotelmanagement

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.samir.core.PreferenceManager
import com.samir.hotelmanagement.navigation.NavKey
import com.samir.hotelmanagement.navigation.goBack
import com.samir.hotelmanagement.ui.booking.BookingDetails
import com.samir.hotelmanagement.ui.booking.BookingListScreen
import com.samir.hotelmanagement.ui.booking.BookingScreen
import com.samir.hotelmanagement.ui.dashboard.MainScreen
import com.samir.hotelmanagement.ui.hotels.HotelDetails
import com.samir.hotelmanagement.ui.hotels.HotelList
import com.samir.hotelmanagement.ui.login.LoginScreen
import com.samir.hotelmanagement.ui.register.RegisterScreen
import org.koin.compose.koinInject

@Composable
fun App() {
    val preferenceManager = koinInject<PreferenceManager>()
    val isLoggedIn = remember {
        preferenceManager.getInt(PreferenceManager.KEY_USER_ID, 0) != 0
    }

    MaterialTheme {
        // Navigation 3 uses a simple observable list as a back stack

        val initialScreen = if (isLoggedIn) NavKey.Main else NavKey.Login
        val backStack = remember { mutableStateListOf<NavKey>(initialScreen) }

        NavDisplay(
            backStack = backStack,
            onBack = { backStack.goBack() },
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300)) togetherWith
                        slideOutHorizontally(
                            targetOffsetX = { -it / 3 },
                            animationSpec = tween(300)
                        ) + fadeOut(animationSpec = tween(300))
            },
            popTransitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { -it / 3 },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300)) togetherWith
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(300)
                        ) + fadeOut(animationSpec = tween(300))
            },
            entryProvider = { key ->
                when (key) {
                    NavKey.Login -> NavEntry(key) {
                        LoginScreen(
                            onLoginSuccess = {
                                backStack.add(NavKey.Main)
                                // Remove everything except the new Main screen to prevent empty backstack crash
                                while (backStack.size > 1) {
                                    backStack.removeAt(0)
                                }
                            },
                            onClickRegister = { backStack.add(NavKey.Register) })
                    }

                    NavKey.Register -> NavEntry(key) {
                        RegisterScreen(
                            onBack = { backStack.goBack() },
                            onRegisterSuccess = {
                                // Handle registration success
                                backStack.goBack()
                            }
                        )
                    }

                    NavKey.Main -> NavEntry(key) {
                        MainScreen(
                            onClickVisitHotels = {
                                backStack.add(NavKey.HotelList)
                            },
                            onClickMyBookings = {
                                backStack.add(NavKey.BookingList)
                            },
                            onLogout = {
                                preferenceManager.clear()
                                backStack.add(NavKey.Login)
                                // Clear stack to Login
                                while (backStack.size > 1) {
                                    backStack.removeAt(0)
                                }
                            }
                        )
                    }

                    NavKey.HotelList -> NavEntry(key) {
                        HotelList({ hotel ->
                            backStack.add(NavKey.HotelDetails(hotel))
                        }) {
                            backStack.goBack()
                        }
                    }

                    is NavKey.HotelDetails -> NavEntry(key) {
                        HotelDetails(
                            hotel = key.hotel,
                            onBack = { backStack.goBack() },
                            onClickBookNow = { hotel ->
                                backStack.add(NavKey.Booking(hotel))
                            },
                        )
                    }

                    is NavKey.Booking -> NavEntry(key) {
                        BookingScreen(
                            hotel = key.hotel,
                            onBack = { backStack.goBack() },
                            onBookingSuccess = {
                                backStack.removeAll { it !is NavKey.Main }
                                backStack.add(NavKey.BookingList)
                            }
                        )
                    }

                    NavKey.BookingList -> NavEntry(key) {
                        BookingListScreen(
                            onBack = { backStack.goBack() },
                            onClickBookingDetails = {
                                backStack.add(NavKey.BookingDetails(it))
                            }
                        )
                    }

                    is NavKey.BookingDetails -> NavEntry(key) {
                        BookingDetails(
                            key.booking,
                            onBack = { backStack.goBack() },
                        )
                    }
                }
            })
    }
}
