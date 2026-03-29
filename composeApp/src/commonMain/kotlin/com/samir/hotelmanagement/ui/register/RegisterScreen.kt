package com.samir.hotelmanagement.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import com.samir.core.BaseResponse
import com.samir.core.User
import com.samir.domain.state.UiState
import com.samir.viewmodels.RegisterViewModel
import hotelmanagement.composeapp.generated.resources.Res
import hotelmanagement.composeapp.generated.resources.ic_arrow_back
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onBack: () -> Unit = {}, onRegisterSuccess: () -> Unit = {}) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val viewModel: RegisterViewModel = koinInject()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    fun validate(): Boolean {
        if (firstName.isBlank()) {
            errorMessage = "First name cannot be blank"
            return false
        }
        if (lastName.isBlank()) {
            errorMessage = "Last name cannot be blank"
            return false
        }
        if (firstName == lastName) {
            errorMessage = "First name and last name cannot be the same"
            return false
        }
        if (email.isBlank()) {
            errorMessage = "Email cannot be blank"
            return false
        }
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$"
        if (!Regex(emailRegex).matches(email)) {
            errorMessage = "Invalid email format"
            return false
        }
        if (phone.length != 10) {
            errorMessage = "Phone number should be 10 chars long"
            return false
        }
        if (password.length < 6) {
            errorMessage = "Password should be at least 6 chars long"
            return false
        }
        if (password != confirmPassword) {
            errorMessage = "Password and confirm password should be same"
            return false
        }
        errorMessage = null
        return true
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 60.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Register",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
            )
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it; errorMessage = null },
                label = { Text("First Name") },
                isError = errorMessage?.contains("first name", ignoreCase = true) == true,
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp, bottom = 16.dp)
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it; errorMessage = null },
                label = { Text("Last Name") },
                isError = errorMessage?.contains("last name", ignoreCase = true) == true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null },
                label = { Text("Email") },
                isError = errorMessage?.contains("email", ignoreCase = true) == true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it; errorMessage = null },
                label = { Text("Phone") },
                isError = errorMessage?.contains("phone", ignoreCase = true) == true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = { Text("Password") },
                isError = errorMessage?.contains("password", ignoreCase = true) == true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; errorMessage = null },
                label = { Text("Confirm Password") },
                isError = errorMessage?.contains("confirm", ignoreCase = true) == true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = {
                    if (validate()) {
                        viewModel.register(
                            firstName,
                            lastName,
                            email,
                            phone,
                            password
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f),
                enabled = uiState !is UiState.Loading,
                shape = RoundedCornerShape(32)
            ) {
                Text("Register")
            }

            ShowUIState(uiState, onRegisterSuccess)
        }
    }
}

@Composable
fun ShowUIState(uiState: UiState<BaseResponse<User>>, onRegisterSuccess: () -> Unit = {}) {
    when (uiState) {

        UiState.Loading -> {

            Spacer(modifier = Modifier.height(16.dp))

            CircularProgressIndicator()
        }

        is UiState.Success -> {
            val data = uiState.data
            if (data.success) {
                Text(
                    text = "Registration Successful...Login Now...",
                    fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(top = 32.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                LaunchedEffect(Unit) {
                    delay(3000)
                    onRegisterSuccess()
                }
            } else {
                Text(
                    text = data.message,
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
