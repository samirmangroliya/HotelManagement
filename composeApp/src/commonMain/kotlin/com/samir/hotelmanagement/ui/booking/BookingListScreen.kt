package com.samir.hotelmanagement.ui.booking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.samir.core.Booking
import com.samir.domain.state.UiState
import com.samir.hotelmanagement.ui.topbar.TopBar
import com.samir.viewmodels.BookingListViewModel
import com.samir.viewmodels.format
import com.samir.viewmodels.toLocalDate
import org.koin.compose.koinInject

@Composable
fun BookingListScreen(
    onBack: () -> Unit,
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
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(bookings) { booking ->
                                BookingItem(booking)
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
fun BookingItem(booking: Booking) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Booking #${booking.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = booking.status,
                    color = when (booking.status) {
                        "Confirmed" -> MaterialTheme.colorScheme.inversePrimary
                        "Pending" -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.error
                    },
                    style = MaterialTheme.typography.labelLarge
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
