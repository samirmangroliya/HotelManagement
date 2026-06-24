package com.samir.hotelmanagement.ui.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.samir.core.Booking
import com.samir.core.format
import com.samir.core.toLocalDate
import com.samir.domain.state.UiState
import com.samir.hotelmanagement.ui.topbar.TopBar
import com.samir.viewmodels.BookingDetailsViewModel
import org.koin.compose.koinInject

@Composable
fun BookingDetails(
    booking: Booking,
    onBack: () -> Unit,
    viewModel: BookingDetailsViewModel = koinInject()
) {
    val hotelState by viewModel.hotelState.collectAsStateWithLifecycle()

    LaunchedEffect(booking.hotelId) {
        viewModel.fetchHotelDetails(booking.hotelId)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Booking Details",
                onBackClick = onBack
            )
        },
        bottomBar = {
            /*Surface(
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
            }*/
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding).fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            when (hotelState) {
                is UiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is UiState.Success -> {
                    val hotel = (hotelState as UiState.Success).data
                    BookingDetailsContent(booking, hotel)
                }

                is UiState.Error -> {
                    Text(
                        text = (hotelState as UiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {}
            }
        }
    }
}

@Composable
fun BookingDetailsContent(booking: Booking, hotel: com.samir.core.Hotel) {
    Column(modifier = Modifier.fillMaxSize().padding(12.dp).verticalScroll(rememberScrollState())) {
        AsyncImage(
            model = hotel.imageUrl,
            contentDescription = hotel.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = hotel.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = hotel.location,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Booking Information",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        DetailItem("Status", booking.status)
        DetailItem("Check-in", booking.checkInDate.toLocalDate().format())
        DetailItem("Check-out", booking.checkOutDate.toLocalDate().format())
        DetailItem("Total Price", "$${booking.totalPrice}")
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}
