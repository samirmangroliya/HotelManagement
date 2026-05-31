package com.samir.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samir.core.Booking
import com.samir.core.PreferenceManager
import com.samir.domain.state.UiState
import com.samir.domain.usercase.GetBookingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookingListViewModel(
    private val getBookingsUseCase: GetBookingsUseCase,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _bookingsState = MutableStateFlow<UiState<List<Booking>>>(UiState.Idle)
    val bookingsState: StateFlow<UiState<List<Booking>>> = _bookingsState

    fun fetchUserBookings() {
        val userId = preferenceManager.getInt(PreferenceManager.KEY_USER_ID, 0)
        if (userId == 0) {
            _bookingsState.value = UiState.Error("User not logged in")
            return
        }

        viewModelScope.launch {
            _bookingsState.value = UiState.Loading
            try {
                val response = getBookingsUseCase(userId)
                if (response.success && response.data != null) {
                    _bookingsState.value = UiState.Success(response.data!!)
                } else {
                    _bookingsState.value = UiState.Error(response.message)
                }
            } catch (e: Exception) {
                _bookingsState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}
