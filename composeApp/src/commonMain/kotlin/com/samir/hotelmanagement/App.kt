package com.samir.hotelmanagement

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.samir.hotelmanagement.ui.dashboard.MainScreen
import com.samir.hotelmanagement.ui.login.LoginScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        var isLoggedIn by remember { mutableStateOf(false) }

        if (isLoggedIn) {
            MainScreen()
        } else {
            LoginScreen(onLoginClicked = { isLoggedIn = true })
        }
    }
}