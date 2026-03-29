package com.samir.hotelmanagement

import androidx.compose.ui.window.ComposeUIViewController
import com.samir.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin {}
    }
) { App() }
