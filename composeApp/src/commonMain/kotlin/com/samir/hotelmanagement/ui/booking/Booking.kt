 package com.samir.hotelmanagement.ui.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.samir.core.Booking
import com.samir.core.Hotel
import com.samir.core.Room
import com.samir.domain.state.UiState
import com.samir.hotelmanagement.ui.topbar.TopBar
import com.samir.viewmodels.BookingViewModel
import org.koin.compose.koinInject

const val DEFAULT_USER_ID = 1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    hotel: Hotel,
    userId: Int = DEFAULT_USER_ID,
    onBack: () -> Unit,
    viewModel: BookingViewModel = koinInject()
) {
    val roomsState by viewModel.roomsState.collectAsState()
    val bookingState by viewModel.bookingState.collectAsState()

    var selectedRoom by remember { mutableStateOf<Room?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    val dateRangePickerState = rememberDateRangePickerState()

    LaunchedEffect(hotel.id) {
        viewModel.fetchRooms(hotel.id)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Book Hotel",
                onBackClick = onBack
            )
        },
        bottomBar = {
            selectedRoom?.let { room ->
                BookingBottomBar(
                    room = room,
                    dateRangePickerState = dateRangePickerState,
                    onBookClick = {
                        val checkIn = dateRangePickerState.selectedStartDateMillis ?: return@BookingBottomBar
                        val checkOut = dateRangePickerState.selectedEndDateMillis ?: return@BookingBottomBar
                        
                        viewModel.bookRoom(
                            Booking(
                                id = 0,
                                userId = userId,
                                roomId = room.id,
                                checkInDate = checkIn.toString(),
                                checkOutDate = checkOut.toString(),
                                totalPrice = room.pricePerNight * 2,
                                status = "Pending"
                            )
                        )
                    },
                    isLoading = bookingState is UiState.Loading
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = hotel.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 10.dp, bottom = 0.dp, start = 20.dp, end = 20.dp)
            )
            Text(
                text = hotel.location,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            DateSelectionCard(dateRangePickerState, viewModel) { showDatePicker = true }

            Text(
                text = "Select Room",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )

            when (roomsState) {
                is UiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                is UiState.Success -> {
                    val rooms = (roomsState as UiState.Success<List<Room>>).data
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(rooms) { room ->
                            RoomCard(
                                room = room,
                                isSelected = selectedRoom?.id == room.id,
                                onSelect = { selectedRoom = room }
                            )
                        }
                    }
                }
                is UiState.Error -> Text("Error loading rooms", color = MaterialTheme.colorScheme.error)
                else -> {}
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("OK") }
                }
            ) {
                DateRangePicker(
                    state = dateRangePickerState,
                    modifier = Modifier.height(400.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelectionCard(state: DateRangePickerState, viewModel: BookingViewModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.DateRange, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Check-in - Check-out", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = viewModel.formatDateRange(state.selectedStartDateMillis, state.selectedEndDateMillis),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RoomCard(room: Room, isSelected: Boolean, onSelect: () -> Unit) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray
    val containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier
            .width(160.dp)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(room.type, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text("$${room.pricePerNight}/night", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(if (room.isAvailable) "Available" else "Booked", 
                style = MaterialTheme.typography.labelSmall,
                color = if (room.isAvailable) Color(0xFF4CAF50) else Color.Red
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingBottomBar(
    room: Room,
    dateRangePickerState: DateRangePickerState,
    onBookClick: () -> Unit,
    isLoading: Boolean
) {
    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 12.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                val checkIn = dateRangePickerState.selectedStartDateMillis
                val checkOut = dateRangePickerState.selectedEndDateMillis
                val nights = if (checkIn != null && checkOut != null) {
                    ((checkOut - checkIn) / (1000 * 60 * 60 * 24)).coerceAtLeast(1)
                } else 1
                
                Text("Total Price ($nights nights)", style = MaterialTheme.typography.labelLarge)
                Text(
                    text = "$${room.pricePerNight * nights}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Button(
                onClick = onBookClick,
                enabled = !isLoading && dateRangePickerState.selectedEndDateMillis != null,
                modifier = Modifier.height(56.dp).width(160.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Confirm")
                }
            }
        }
    }
}
