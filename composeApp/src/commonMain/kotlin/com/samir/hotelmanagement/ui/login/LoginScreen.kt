package com.samir.hotelmanagement.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.samir.hotelmanagement.domain.state.UiState
import com.samir.hotelmanagement.viewmodels.LoginViewModel
import com.samir.network.models.BaseResponse
import com.samir.network.models.User
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit = {}, onClickRegister: () -> Unit = {}) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val viewModel: LoginViewModel = koinInject()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    fun validate(): Boolean {
        if (email.isBlank()) {
            errorMessage = "Email cannot be blank"
            return false
        }
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$"
        if (!Regex(emailRegex).matches(email)) {
            errorMessage = "Invalid email format"
            return false
        }
        if (password.length < 6) {
            errorMessage = "Password should be at least 6 chars long"
            return false
        }
        errorMessage = null
        return true
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login Now",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorMessage = null },
            label = { Text("Email") },
            isError = errorMessage?.contains("email", ignoreCase = true) == true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMessage = null },
            label = { Text("Password") },
            isError = errorMessage?.contains("password", ignoreCase = true) == true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                if (validate()) {
                    viewModel.login(
                        email,
                        password
                    )
                }
            },
            enabled = uiState !is UiState.Loading,
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = RoundedCornerShape(32)
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onClickRegister,
            enabled = uiState !is UiState.Loading,
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = RoundedCornerShape(32)
        ) {
            Text("Register")
        }
        showUIState(uiState, onLoginSuccess)
    }
}


@Composable
fun showUIState(uiState: UiState<BaseResponse<User>>, onLoginSuccess: () -> Unit = {}) {
    when (uiState) {

        UiState.Loading -> {

            Spacer(modifier = Modifier.height(16.dp))

            CircularProgressIndicator()
        }

        is UiState.Success -> {
            val data = uiState.data
            if (data.success) {
                Text(
                    text = "Login Successful...Redirecting...",
                    fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(top = 32.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                LaunchedEffect(Unit) {
                    delay(3000)
                    onLoginSuccess()
                }
            } else {
                Text(
                    text = "Login Failed..." + data.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is UiState.Error -> {

            Text(
                text = uiState.message,
                color = MaterialTheme.colorScheme.error
            )
        }

        else -> {}
    }
}
