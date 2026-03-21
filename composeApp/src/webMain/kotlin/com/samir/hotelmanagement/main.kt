package com.samir.hotelmanagement

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.samir.hotelmanagement.di.initKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin {}
    ComposeViewport {
        App()
    }
}