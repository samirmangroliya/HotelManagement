package com.samir.hotelmanagement.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
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
@Preview
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit = {}, onClickRegister: () -> Unit = {}) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegisterSuccess = mutableStateOf(false)

    val viewModel: LoginViewModel = koinInject()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                viewModel.login(
                    email,
                    password
                )
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
                    text = "Login Failed..."+data.message,
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
