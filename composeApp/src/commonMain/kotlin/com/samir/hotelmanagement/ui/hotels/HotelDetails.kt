package com.samir.hotelmanagement.ui.hotels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.samir.hotelmanagement.ui.topbar.TopBar
import com.samir.viewmodels.HotelViewModel
import hotelmanagement.composeapp.generated.resources.Res
import hotelmanagement.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelDetails(hotelId: Int) {
    Scaffold(
        topBar = { TopBar(stringResource(Res.string.app_name)) }
    ) { innerPadding ->
        val hotelViewModel: HotelViewModel = koinInject()

        Column(
            modifier = Modifier.padding(innerPadding)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Hotel ID: $hotelId")

            Button(onClick = {
                
            }) {
                Text("Visit Hotels")
            }

        }
    }
}