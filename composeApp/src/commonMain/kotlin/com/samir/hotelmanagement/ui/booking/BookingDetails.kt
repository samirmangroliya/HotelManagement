package com.samir.hotelmanagement.ui.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.samir.core.Booking
import com.samir.hotelmanagement.ui.topbar.TopBar

@Composable
fun BookingDetails(booking: Booking, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Booking Details",
                onBackClick = onBack
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 2.dp,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth().navigationBarsPadding()
            ) {
                Button(
                    onClick = { onBack() },
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding).fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            BookingDetailsContent(booking)
        }
    }
}

@Composable
fun BookingDetailsContent(booking: Booking) {
    val hotelId = booking
    Column(modifier = Modifier.fillMaxSize().padding(8.dp)){

    }
}