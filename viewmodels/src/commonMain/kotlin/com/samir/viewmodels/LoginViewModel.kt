package com.samir.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samir.core.BaseResponse
import com.samir.domain.usercase.LoginUseCase
import com.samir.core.User
import com.samir.domain.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
   private val loginUseCase: LoginUseCase
) : ViewModel() {

   private val _uiState =
      MutableStateFlow<UiState<BaseResponse<User>>>(UiState.Idle)

   val uiState: StateFlow<UiState<BaseResponse<User>>> = _uiState

   fun login(
      email: String,
      password: String
   ) {

      viewModelScope.launch {

         _uiState.value = UiState.Loading

         try {

            val response = loginUseCase(email, password)

            _uiState.value = UiState.Success(response)

         } catch (e: Exception) {

            _uiState.value =
               UiState.Error(e.message ?: "Unknown error, Try Again Later...")
         }
      }
   }
}
