package com.samir.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samir.core.Booking
import com.samir.core.Room
import com.samir.domain.state.UiState
import com.samir.domain.usercase.CreateBookingUseCase
import com.samir.domain.usercase.GetRoomsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlin.time.Instant
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

class BookingViewModel(
    private val getRoomsUseCase: GetRoomsUseCase,
    private val createBookingUseCase: CreateBookingUseCase
) : ViewModel() {

    private val _roomsState = MutableStateFlow<UiState<List<Room>>>(UiState.Idle)
    val roomsState: StateFlow<UiState<List<Room>>> = _roomsState

    private val _bookingState = MutableStateFlow<UiState<Booking>>(UiState.Idle)
    val bookingState: StateFlow<UiState<Booking>> = _bookingState

    fun fetchRooms(hotelId: Int) {
        viewModelScope.launch {
            _roomsState.value = UiState.Loading
            try {
                // Using dummy data as requested
                val dummyRooms = listOf(
                    Room(1, hotelId, "101", "Standard Room", 100.0),
                    Room(2, hotelId, "102", "Deluxe Room", 180.0),
                    Room(3, hotelId, "201", "Executive Suite", 300.0),
                    Room(4, hotelId, "301", "Presidential Suite", 550.0),
                    Room(5, hotelId, "105", "Single Room", 80.0)
                )
                
                // You can still call the use case and use dummy as fallback or vice versa
                // For now, we directly set the dummy data
                _roomsState.value = UiState.Success(dummyRooms)
                
                /* Real implementation:
                val response = getRoomsUseCase(hotelId)
                if (response.success && response.data != null) {
                    _roomsState.value = UiState.Success(response.data!!)
                } else {
                    _roomsState.value = UiState.Error(response.message)
                }
                */
            } catch (e: Exception) {
                _roomsState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun bookRoom(booking: Booking) {
        viewModelScope.launch {
            _bookingState.value = UiState.Loading
            try {
                val response = createBookingUseCase(booking)
                if (response.success && response.data != null) {
                    _bookingState.value = UiState.Success(response.data!!)
                } else {
                    _bookingState.value = UiState.Error(response.message)
                }
            } catch (e: Exception) {
                _bookingState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun formatDateRange(start: Long?, end: Long?): String {
        if (start == null) return "Select check in date"
        val startStr = toFormattedDate(start)
        if (end == null) return "Select check out date"
        return "$startStr - ${toFormattedDate(end)}"
    }

    private fun toFormattedDate(millis: Long): String {
        val instant = Instant.fromEpochMilliseconds(millis)
        val date = instant.toLocalDateTime(TimeZone.UTC).date
        val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        return "${date.day} ${monthNames[date.month.number - 1]}"
    }

}
