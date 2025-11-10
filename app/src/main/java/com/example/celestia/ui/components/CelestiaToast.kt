package com.example.celestia.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay

@Composable
fun CelestiaToast(
    message: String,
    icon: @Composable (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
    textColor: Color = Color.White,
    durationMs: Long = 2500L
) {
    var visible by remember { mutableStateOf(true) }

    if (visible) {
        LaunchedEffect(Unit) {
            delay(durationMs)
            visible = false
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = backgroundColor,
                tonalElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    icon?.invoke()
                    Text(
                        text = message,
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}
