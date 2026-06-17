package com.samir.hotelmanagement.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samir.core.BaseResponse
import com.samir.core.Hotel
import com.samir.domain.state.UiState
import com.samir.domain.usercase.HotelUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HotelViewModel(
   private val hotelUseCase: HotelUseCase
) : ViewModel() {

   private val _uiState =
      MutableStateFlow<UiState<BaseResponse<List<Hotel>>>>(UiState.Idle)

   val uiState: StateFlow<UiState<BaseResponse<List<Hotel>>>> = _uiState

   var scrollIndex: Int = 0
   var scrollOffset: Int = 0

   fun fetchHotels() {
      if (_uiState.value is UiState.Success || _uiState.value is UiState.Loading) return
      viewModelScope.launch {
         _uiState.value = UiState.Loading
         try {
            val hotels = hotelUseCase.invoke()
            if(hotels.data?.isNotEmpty() == true) {
               val hotelResponse = BaseResponse(
                  success = true,
                  message = "Hotels fetched successfully",
                  data = hotels
               )
               _uiState.value = UiState.Success(hotelResponse)
            } else {
               val hotelResponse = BaseResponse(
                  success = false,
                  message = "No Hotels found....",
                  data = null
               )
               _uiState.value = UiState.Success(hotelResponse)
            }


         } catch (e: Exception) {
            _uiState.value =
               UiState.Error(e.message ?: "Unknown error, Try Again Later...")
         }
      }
   }
}
