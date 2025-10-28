package com.example.spaceweathermonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.spaceweathermonitor.ui.viewmodel.SpaceWeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    vm: SpaceWeatherViewModel = viewModel()
) {
    val readings by vm.readings.observeAsState(emptyList())
    val lastUpdated by vm.lastUpdated.observeAsState("Never")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Space Weather Dashboard") }) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (readings.isNotEmpty()) {
                val latest = readings.first()
                val kp = latest.kpIndex
                val high = readings.maxOfOrNull { it.kpIndex } ?: kp
                val low = readings.minOfOrNull { it.kpIndex } ?: kp

                val (status, color, message) = when {
                    kp >= 7 -> Triple("Severe Storm", Color(0xFFB71C1C), "Major geomagnetic storm — auroras visible far south!")
                    kp >= 5 -> Triple("Active Storm", Color(0xFFFFA000), "Aurora likely visible in northern skies.")
                    kp >= 3 -> Triple("Active", Color(0xFF388E3C), "Minor aurora activity possible near polar regions.")
                    else -> Triple("Quiet", Color(0xFF1976D2), "Calm conditions, no aurora expected.")
                }

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(containerColor = color.copy(alpha = 0.15f))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Current Kp Index: $kp", color = color, style = MaterialTheme.typography.titleLarge)
                        Text("Status: $status", color = color)
                        Text(message)
                        Spacer(Modifier.height(8.dp))
                        Text("High: $high | Low: $low")
                        Text("Last updated: $lastUpdated")
                        Spacer(Modifier.height(8.dp))
                        if (lastUpdated == "Never") {
                            Text(
                                text ="*Warning*",
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                color = Color(0xFFEF6C00),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Data may be outdated. Please refresh.",
                                color = Color(0xFFEF6C00),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }

                    }
                }
            } else {
                Text("No data loaded yet. Tap Reload to fetch current conditions.")
            }

            HorizontalDivider()

            Text(
                "Kp Scale: 0–2 Quiet | 3–4 Active | 5–6 Storm | 7–9 Severe Storm",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "The Kp Index measures disturbances in Earth's magnetic field caused by solar activity. " +
                        "Higher Kp values mean stronger geomagnetic storms and a greater chance of seeing auroras.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("data") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("View All Records")
            }
            Button(
                onClick = { vm.refresh() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ){
                Text("Reload")
            }
        }
    }
}
