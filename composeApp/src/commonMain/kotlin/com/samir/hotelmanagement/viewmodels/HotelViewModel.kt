package com.samir.hotelmanagement.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HotelViewModel : ViewModel() {
   private val _uiState = MutableStateFlow(OrderUiState())
   val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

   val currentHotelName = MutableStateFlow("Hilton Hotel").asStateFlow()

}

class OrderUiState(var price: Double? = 0.0)
