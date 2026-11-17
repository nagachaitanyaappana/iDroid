package com.example.idroid.ui.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun IosWidgetSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Left Widget: Clock & Date
        ClockWidget(modifier = Modifier.weight(1f))

        // Right Widget: Weather & Battery
        WeatherWidget(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ClockWidget(modifier: Modifier = Modifier) {
    val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    val dateFormat = SimpleDateFormat("MMMM d", Locale.getDefault())
    val now = Date()

    Box(
        modifier = modifier
            .height(140.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White.copy(alpha = 0.25f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.35f),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = dayFormat.format(now).uppercase(),
                color = Color(0xFFFF3B30),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = dateFormat.format(now),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "iDroid iOS Theme",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun WeatherWidget(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(140.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xFF2C5282).copy(alpha = 0.85f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Cupertino",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "72°",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Sunny",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "H:78°  L:55°",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
        }
    }
}
