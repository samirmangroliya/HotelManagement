package com.samir.hotelmanagement

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.samir.hotelmanagement.navigation.NavKey
import com.samir.hotelmanagement.ui.dashboard.MainScreen
import com.samir.hotelmanagement.ui.hotels.HotelDetails
import com.samir.hotelmanagement.ui.hotels.HotelList
import com.samir.hotelmanagement.ui.login.LoginScreen
import com.samir.hotelmanagement.ui.register.RegisterScreen

@Composable
fun App() {
    MaterialTheme {
        // Navigation 3 uses a simple observable list as a back stack
        val backStack = remember { mutableStateListOf<NavKey>(NavKey.Login) }

        NavDisplay(backStack = backStack, onBack = {
            if (backStack.size > 1) {
                backStack.removeAt(backStack.size - 1)
            }
        }, entryProvider = { key ->
            when (key) {
               is NavKey.Login -> NavEntry(key) {
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

                is NavKey.Register -> NavEntry(key) {
                    RegisterScreen(onBack = {
                        if (backStack.size > 1) {
                            backStack.removeAt(backStack.size - 1)
                        }
                    }, onRegisterSuccess = {
                        // Handle registration success
                        if (backStack.size > 1) {
                            backStack.removeAt(backStack.size - 1)
                        }
                    })
                }

                is NavKey.Main -> NavEntry(key) {
                    MainScreen {
                        backStack.add(NavKey.HotelList)
                    }
                }

                is NavKey.HotelList -> NavEntry(key) {
                    HotelList { hotelId ->
                        backStack.add(NavKey.HotelDetails(hotelId))
                    }
                }

                is NavKey.HotelDetails -> NavEntry(key) {
                    HotelDetails(key.hotelId)
                }
            }
        })
    }
}
