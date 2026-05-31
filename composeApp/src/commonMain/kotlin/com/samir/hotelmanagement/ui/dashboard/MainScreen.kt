package com.samir.hotelmanagement.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.samir.hotelmanagement.ui.topbar.TopBar
import hotelmanagement.composeapp.generated.resources.Res
import hotelmanagement.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource

/**
 * Main dashboard screen providing access to key app features.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onClickVisitHotels: () -> Unit,
    onClickMyBookings: () -> Unit,
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    // State-driven Dialog handling
    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                showLogoutDialog = false
                onLogout()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    Scaffold(
        topBar = { TopBar(stringResource(Res.string.app_name)) }
    ) { innerPadding ->
        MainContent(
            modifier = Modifier.padding(innerPadding),
            onClickVisitHotels = onClickVisitHotels,
            onClickMyBookings = onClickMyBookings,
            onLogoutRequest = { showLogoutDialog = true }
        )
    }
}

/**
 * Encapsulates the main layout and content of the dashboard.
 */
@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    onClickVisitHotels: () -> Unit,
    onClickMyBookings: () -> Unit,
    onLogoutRequest: () -> Unit
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .safeContentPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(32.dp))

        DashboardButton(
            text = "Visit Hotels",
            icon = Icons.Default.Hotel,
            onClick = onClickVisitHotels
        )

        Spacer(modifier = Modifier.height(16.dp))

        DashboardButton(
            text = "My Bookings",
            icon = Icons.Default.BookOnline,
            onClick = onClickMyBookings
        )

        Spacer(modifier = Modifier.height(48.dp))

        LogoutButton(onClick = onLogoutRequest)
    }
}

/**
 * Reusable styled button for dashboard actions.
 */
@Composable
private fun DashboardButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier
            .fillMaxWidth(0.85f)
            .height(60.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * Specialized button for the logout action.
 */
@Composable
private fun LogoutButton(onClick: () -> Unit) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(56.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.error
        ),
        border = ButtonDefaults.outlinedButtonBorder(true).copy(
            brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.error)
        )
    ) {
        Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = null)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Logout",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * Dialog for confirming user logout.
 */
@Composable
private fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Logout") },
        text = { Text("Are you sure you want to end your session?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Logout")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
