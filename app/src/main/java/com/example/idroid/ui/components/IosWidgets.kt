package com.example.idroid.ui.components

import android.os.BatteryManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun IosWidgetSection(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val batteryManager = remember {
        context.getSystemService(BatteryManager::class.java)
    }
    var batteryLevel by remember {
        mutableIntStateOf(
            batteryManager?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                ?.takeIf { it in 0..100 } ?: -1
        )
    }

    var currentTime by remember { mutableStateOf(formatTime()) }
    var currentDate by remember { mutableStateOf(formatDate()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = formatTime()
            currentDate = formatDate()
            batteryLevel = batteryManager
                ?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                ?.takeIf { it in 0..100 } ?: -1
            delay(30_000L)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // iOS-style clock and calendar widget
        Column(
            modifier = Modifier
                .weight(1.12f)
                .height(142.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(Color(0xFF18283E).copy(alpha = 0.66f))
                .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(25.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currentDate.uppercase(Locale.getDefault()),
                color = Color(0xFFFF6B72),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = currentTime,
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = (-1).sp
            )
            Text(
                text = "CALENDAR",
                color = Color.White.copy(alpha = 0.72f),
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.8.sp
            )
        }

        // Live battery widget (not a made-up weather reading)
        Column(
            modifier = Modifier
                .weight(0.88f)
                .height(116.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(Color(0xFF254D55).copy(alpha = 0.78f))
                .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(25.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "BATTERIES",
                color = Color.White.copy(alpha = 0.72f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = if (batteryLevel >= 0) "$batteryLevel%" else "—",
                color = Color.White,
                fontSize = 31.sp,
                fontWeight = FontWeight.Light
            )
            Text(
                text = "This device",
                color = Color.White.copy(alpha = 0.72f),
                fontSize = 10.sp
            )
        }
    }
}

private fun formatTime(): String =
    SimpleDateFormat("h:mm", Locale.getDefault()).format(Date())

private fun formatDate(): String =
    SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date())
