package com.example.celestia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlin.math.abs
import kotlin.math.roundToInt
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

// --- simple UI state to keep this self-contained for now ---
data class IssUiState(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val altitudeKm: Double = 0.0,
    val velocityKmh: Double = 0.0,
    val crewCount: Int = 7, // placeholder
    val updated: String = "--:--"
)

class IssViewModel : ViewModel() {
    // placeholder values (so the UI looks “real” before wiring an API)
    var ui by mutableStateOf(
        IssUiState(
            latitude = 42.3601,
            longitude = -71.0589,
            altitudeKm = 408.5,
            velocityKmh = 27580.0,
            crewCount = 7,
            updated = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("MMM d, HH:mm"))
        )
    )
        private set

    // call this later from a repository using an ISS API
    fun refreshWith(
        lat: Double,
        lon: Double,
        altKm: Double,
        velKmh: Double,
        crew: Int?,
        updatedText: String
    ) {
        ui = ui.copy(
            latitude = lat,
            longitude = lon,
            altitudeKm = altKm,
            velocityKmh = velKmh,
            crewCount = crew ?: ui.crewCount,
            updated = updatedText
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssLocationScreen(
    navController: NavController,
    vm: IssViewModel = viewModel()
) {
    val ui = vm.ui

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ISS Location") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // === Main ISS data card ===
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                )
            ) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // Header
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.9f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = "ISS",
                                tint = Color.White
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                "International Space Station",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                "Live Position",
                                color = Color.LightGray,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    HorizontalDivider(Modifier.padding(vertical = 8.dp))

                    StatRow(
                        icon = Icons.Default.LocationOn,
                        label = "Coordinates",
                        value = "${formatCoord(ui.latitude, 'N', 'S')}, " +
                                "${formatCoord(ui.longitude, 'E', 'W')}"
                    )
                    StatRow(
                        icon = Icons.Default.Public,
                        label = "Altitude",
                        value = "${formatNumber(ui.altitudeKm)} km"
                    )
                    StatRow(
                        icon = Icons.Default.Speed,
                        label = "Velocity",
                        value = "${formatNumber(ui.velocityKmh)} km/h"
                    )
                    StatRow(
                        icon = Icons.Default.Public,
                        label = "Current Crew",
                        value = "${ui.crewCount} astronaut${if (ui.crewCount == 1) "" else "s"}"
                    )

                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Updated: ${ui.updated}",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            // === Map / Orbit Placeholder ===
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                )
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(CircleShape)
                                .background(
                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Public,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Interactive Map Visualization",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Real-time tracking coming soon",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // === New Info Card (Launch + Mass) ===
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                )
            ) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "ISS Specifications",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Mini-card 1: Mass
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp)),
                            tonalElevation = 3.dp,
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Column(
                                Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Mass", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                                Text("420,000 kg", style = MaterialTheme.typography.titleMedium)
                            }
                        }

                        // Mini-card 2: Launch Date
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp)),
                            tonalElevation = 3.dp,
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Column(
                                Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Launch Date", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                                Text("Nov 20, 1998", style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun StatRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

private fun formatCoord(deg: Double, pos: Char, neg: Char): String {
    val hemi = if (deg >= 0) pos else neg
    val absDeg = abs(deg)
    return "${"%.4f".format(absDeg)}° $hemi"
}

private fun formatNumber(n: Double): String {
    // 27,580 -> with thousands separators, keep one decimal if needed
    val rounded = if ((n * 10).roundToInt() % 10 == 0) n.toInt().toString() else "%.1f".format(n)
    val parts = rounded.split(".")
    val intPart = parts[0]
    val withCommas = intPart.reversed().chunked(3).joinToString(",").reversed()
    return if (parts.size == 2) "$withCommas.${parts[1]}" else withCommas
}
