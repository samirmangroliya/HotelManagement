package com.samir.hotelmanagement.ui.booking

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.samir.core.Booking
import com.samir.core.format
import com.samir.core.toLocalDate
import com.samir.domain.state.UiState
import com.samir.hotelmanagement.theme.AppColors
import com.samir.hotelmanagement.ui.topbar.TopBar
import com.samir.viewmodels.BookingListViewModel
import org.koin.compose.koinInject

@Composable
fun BookingListScreen(
    onBack: () -> Unit,
    onClickBookingDetails: (Booking) -> Unit,
    viewModel: BookingListViewModel = koinInject()
) {
    val bookingsState by viewModel.bookingsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchUserBookings()
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "My Bookings",
                onBackClick = onBack
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (bookingsState) {
                is UiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is UiState.Success -> {
                    val bookings = (bookingsState as UiState.Success<List<Booking>>).data
                    if (bookings.isEmpty()) {
                        Text(
                            "No bookings found",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(bookings) { booking ->
                                BookingItem(booking) {
                                    onClickBookingDetails(it)
                                }
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Text(
                        (bookingsState as UiState.Error).message,
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
fun BookingItem(booking: Booking, onClickBookingDetails:(Booking) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp).clickable{
            onClickBookingDetails(booking)
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Booking No. ${booking.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = booking.status,
                    color = when {
                        booking.status.equals("confirmed", ignoreCase = true) -> AppColors.Success
                        booking.status.equals("pending", ignoreCase = true) -> AppColors.Warning
                        else -> AppColors.Error
                    },
                    style = MaterialTheme.typography.labelLarge
                )
                Image(
                    imageVector = Icons.Default.ArrowCircleRight,
                    contentDescription = "Navigate",
                    modifier = Modifier.size(24.dp).clickable {
                        onClickBookingDetails(booking)
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Check-in: ${booking.checkInDate.toLocalDate().format()}", style = MaterialTheme.typography.bodyMedium)
            Text("Check-out: ${booking.checkOutDate.toLocalDate().format()}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total: $${booking.totalPrice}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
