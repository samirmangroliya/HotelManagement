package com.samir.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samir.core.Booking
import com.samir.core.PreferenceManager
import com.samir.core.Room
import com.samir.domain.state.UiState
import com.samir.domain.usercase.CreateBookingUseCase
import com.samir.domain.usercase.GetRoomsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

class BookingViewModel(
    private val roomUsecase: GetRoomsUseCase,
    private val createBookingUseCase: CreateBookingUseCase,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _roomsState = MutableStateFlow<UiState<List<Room>>>(UiState.Idle)
    val roomsState: StateFlow<UiState<List<Room>>> = _roomsState

    private val _bookingState = MutableStateFlow<UiState<Booking>>(UiState.Idle)
    val bookingState: StateFlow<UiState<Booking>> = _bookingState

    fun fetchRooms(hotelId: Int) {
        viewModelScope.launch {
            _roomsState.update { UiState.Loading }
            try {
                val rooms = roomUsecase.invoke(hotelId)
                if(rooms.data?.isNotEmpty() == true) {
                    _roomsState.update { UiState.Success(rooms.data ?: emptyList()) }
                } else {
                    _roomsState.update { UiState.Success(emptyList()) }
                }
            } catch (e: Exception) {
                _roomsState.update {
                    UiState.Error(e.message ?: "Unknown error, Try Again Later...")
                }
            }
        }
    }

    fun bookRoom(room: Room, checkIn: Long, checkOut: Long) {
        val userId = preferenceManager.getInt(PreferenceManager.KEY_USER_ID, 0)
        val totalDays = if (checkIn != null && checkOut != null) {
            ((checkOut - checkIn) / (1000 * 60 * 60 * 24)).coerceAtLeast(1)
        } else 1

        val booking = Booking(
            id = 0,
            userId = userId,
            roomId = room.id,
            checkInDate = checkIn.toString(),
            checkOutDate = checkOut.toString(),
            totalPrice = room.pricePerNight *totalDays,
            totalDay = totalDays.toInt(),
            status = "Pending"
        )
        viewModelScope.launch {
            _bookingState.update { UiState.Loading }
            try {
                val response = createBookingUseCase(booking)
                if (response.success && response.data != null) {
                    _bookingState.update { UiState.Success(response.data!!) }
                } else {
                    _bookingState.update { UiState.Error(response.message) }
                }
            } catch (e: Exception) {
                _bookingState.update { UiState.Error(e.message ?: "Unknown error occurred") }
            }
        }
    }

    fun formatDateRange(start: Long?, end: Long?): String {
        if (start == null) return "Select check in date"
        val startStr = start.toLocalDate().format()
        if (end == null) return "Select check out date"
        return "$startStr - ${end.toLocalDate().format()}"
    }

}

fun Long.toLocalDate(): LocalDate {
    return Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
}

fun String.toLocalDate(): LocalDate {
    return toLong().toLocalDate()
}

fun LocalDate.format(): String {
    val dayStr = day.toString().padStart(2, '0')
    val monthStr = month.number.toString().padStart(2, '0')
    return "$dayStr/$monthStr/$year"
}
