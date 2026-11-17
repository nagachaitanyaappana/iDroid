package com.example.idroid.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IosControlCenter(
    isOpen: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isOpen,
        enter = fadeIn() + slideInVertically { -it },
        exit = fadeOut() + slideOutVertically { -it }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
                .clickable { onClose() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0xFF1C1C1E).copy(alpha = 0.95f))
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(32.dp)
                    )
                    .clickable(enabled = false) {}
                    .padding(20.dp)
            ) {
                // Header bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Control Center",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable { onClose() }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Done",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Connectivity & Media Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Connectivity Tile
                    ConnectivityTile(modifier = Modifier.weight(1f))

                    // Music Tile
                    MusicTile(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Brightness & Volume Sliders
                SlidersRow()

                Spacer(modifier = Modifier.height(16.dp))

                // Quick Action Shortcuts Grid
                QuickShortcutsGrid()
            }
        }
    }
}

@Composable
private fun ConnectivityTile(modifier: Modifier = Modifier) {
    var wifiOn by remember { mutableStateOf(true) }
    var bluetoothOn by remember { mutableStateOf(true) }
    var cellularOn by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .height(150.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .padding(14.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ToggleCircle(
                    label = "Wi-Fi",
                    active = wifiOn,
                    onClick = { wifiOn = !wifiOn }
                )
                ToggleCircle(
                    label = "BT",
                    active = bluetoothOn,
                    onClick = { bluetoothOn = !bluetoothOn }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ToggleCircle(
                    label = "Cellular",
                    active = cellularOn,
                    onClick = { cellularOn = !cellularOn }
                )
                ToggleCircle(
                    label = "Air",
                    active = false,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
private fun ToggleCircle(
    label: String,
    active: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(if (active) Color(0xFF007AFF) else Color.White.copy(alpha = 0.18f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun MusicTile(modifier: Modifier = Modifier) {
    var isPlaying by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .height(150.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Music Player",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isPlaying) "Playing iOS Track" else "Not Playing",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFFA2D48))
                    .clickable { isPlaying = !isPlaying }
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (isPlaying) "PAUSE" else "PLAY",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SlidersRow() {
    var brightness by remember { mutableFloatStateOf(0.7f) }
    var volume by remember { mutableFloatStateOf(0.5f) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Brightness
        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.12f))
                .padding(14.dp)
        ) {
            Text(
                text = "Brightness",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Slider(
                value = brightness,
                onValueChange = { brightness = it },
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                )
            )
        }

        // Volume
        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.12f))
                .padding(14.dp)
        ) {
            Text(
                text = "Volume",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Slider(
                value = volume,
                onValueChange = { volume = it },
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                )
            )
        }
    }
}

@Composable
private fun QuickShortcutsGrid() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val shortcuts = listOf("Torch", "Camera", "Calc", "LowPower")
        shortcuts.forEach { label ->
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
