package com.samir.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samir.core.Hotel
import com.samir.domain.state.UiState
import com.samir.domain.usercase.GetHotelByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookingDetailsViewModel(
    private val getHotelByIdUseCase: GetHotelByIdUseCase
) : ViewModel() {

    private val _hotelState = MutableStateFlow<UiState<Hotel>>(UiState.Idle)
    val hotelState: StateFlow<UiState<Hotel>> = _hotelState

    fun fetchHotelDetails(hotelId: Int) {
        viewModelScope.launch {
            _hotelState.value = UiState.Loading
            try {
                val response = getHotelByIdUseCase(hotelId)
                if (response.success && response.data != null) {
                    _hotelState.value = UiState.Success(response.data!!)
                } else {
                    _hotelState.value = UiState.Error(response.message)
                }
            } catch (e: Exception) {
                _hotelState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}
