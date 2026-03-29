package com.samir.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samir.core.BaseResponse
import com.samir.domain.usercase.RegisterUseCase
import com.samir.core.User
import com.samir.domain.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<BaseResponse<User>>>(UiState.Idle)

    val uiState: StateFlow<UiState<BaseResponse<User>>> = _uiState

    fun register(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        password: String
    ) {

        viewModelScope.launch {

            _uiState.value = UiState.Loading

            try {

                val response = registerUseCase(
                    firstName,
                    lastName,
                    email,
                    phone,
                    password
                )

                _uiState.value = UiState.Success(response)

            } catch (e: Exception) {

                _uiState.value =
                    UiState.Error(e.message ?: "Unknown error, Try Again Later...")
            }
        }
    }
}
