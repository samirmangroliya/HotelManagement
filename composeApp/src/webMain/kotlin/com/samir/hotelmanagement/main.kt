package com.samir.hotelmanagement

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.samir.di.initKoin


@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin {}
    ComposeViewport {
        App()
    }
}