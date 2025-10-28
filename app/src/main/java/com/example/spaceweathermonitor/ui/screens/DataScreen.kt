package com.example.spaceweathermonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.spaceweathermonitor.ui.viewmodel.SpaceWeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataScreen(
    navController: NavController,
    vm: SpaceWeatherViewModel = viewModel()
) {
    val readings by vm.readings.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Kp Records") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.KeyboardDoubleArrowLeft, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (readings.isEmpty()) {
                Text("No data available. Return to the home screen and tap Reload.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(readings) { reading ->
                        val color = when {
                            reading.kpIndex >= 7 -> Color(0xFFB71C1C)
                            reading.kpIndex >= 5 -> Color(0xFFFFA000)
                            reading.kpIndex >= 3 -> Color(0xFF388E3C)
                            else -> Color(0xFF1976D2)
                        }

                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = color.copy(alpha = 0.1f)
                            )
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Text("Time: ${reading.timestamp}")
                                Text("Kp Index: ${reading.kpIndex}", color = color)
                            }
                        }
                    }
                }
            }
        }
    }
}
