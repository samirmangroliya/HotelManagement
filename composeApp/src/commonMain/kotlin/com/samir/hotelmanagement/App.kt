package com.samir.hotelmanagement

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.samir.hotelmanagement.navigation.NavKey
import com.samir.hotelmanagement.ui.dashboard.MainScreen
import com.samir.hotelmanagement.ui.login.LoginScreen
import com.samir.hotelmanagement.ui.login.RegisterScreen

@Composable
fun App() {
    MaterialTheme {
        // Navigation 3 uses a simple observable list as a back stack
        val backStack = remember { mutableStateListOf<NavKey>(NavKey.Login) }

        NavDisplay(backStack = backStack, onBack = {
            if (backStack.isNotEmpty()) {
                backStack.removeAt(backStack.size - 1)
            }
        }, entryProvider = { key ->
            when (key) {
                NavKey.Login -> NavEntry(key) {
                    LoginScreen(
                        onLoginSuccess = { backStack.add(NavKey.Main) },
                        onClickRegister = { backStack.add(NavKey.Register) })
                }

                NavKey.Register -> NavEntry(key) {
                    RegisterScreen(onBack = {
                        if (backStack.isNotEmpty()) {
                            backStack.removeAt(backStack.size - 1)
                        }
                    }, onRegisterClicked = {
                        // Handle registration success
                        if (backStack.isNotEmpty()) {
                            backStack.removeAt(backStack.size - 1)
                        }
                    })
                }

                NavKey.Main -> NavEntry(key) {
                    MainScreen()
                }
            }
        })
    }
}
