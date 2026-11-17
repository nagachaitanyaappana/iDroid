package com.example.idroid.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun IosStatusHeader(
    onOpenControlCenter: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTime by remember { mutableStateOf(getFormattedTime()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = getFormattedTime()
            delay(1000L)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Time
        Text(
            text = currentTime,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )

        // Right: iOS Signal, Wifi, Battery & Control Center Pill
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IosSignalIcon()
            IosWifiIcon()
            IosBatteryIcon()

            // Control Center Trigger Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.25f))
                    .clickable { onOpenControlCenter() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Control",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun IosSignalIcon() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.height(12.dp)
    ) {
        val heights = listOf(4.dp, 6.dp, 9.dp, 12.dp)
        heights.forEach { h ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(h)
                    .clip(RoundedCornerShape(1.dp))
                    .background(Color.White)
            )
        }
    }
}

@Composable
private fun IosWifiIcon() {
    Canvas(modifier = Modifier.size(14.dp)) {
        val stroke = 2.dp.toPx()
        drawArc(
            color = Color.White,
            startAngle = 220f,
            sweepAngle = 100f,
            useCenter = false,
            topLeft = Offset(0f, 0f),
            size = Size(size.width, size.height),
            style = androidx.compose.ui.graphics.drawscope.Stroke(stroke)
        )
        drawArc(
            color = Color.White,
            startAngle = 220f,
            sweepAngle = 100f,
            useCenter = false,
            topLeft = Offset(size.width * 0.25f, size.height * 0.25f),
            size = Size(size.width * 0.5f, size.height * 0.5f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(stroke)
        )
        drawCircle(
            color = Color.White,
            radius = stroke / 1.5f,
            center = Offset(size.width * 0.5f, size.height * 0.85f)
        )
    }
}

@Composable
private fun IosBatteryIcon() {
    Canvas(modifier = Modifier.size(width = 22.dp, height = 11.dp)) {
        val corner = 3.dp.toPx()
        // Outer body
        drawRoundRect(
            color = Color.White.copy(alpha = 0.6f),
            cornerRadius = CornerRadius(corner, corner),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5.dp.toPx())
        )
        // Inner level (80%)
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(2.dp.toPx(), 2.dp.toPx()),
            size = Size((size.width - 6.dp.toPx()) * 0.8f, size.height - 4.dp.toPx()),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )
        // Cap
        drawRoundRect(
            color = Color.White.copy(alpha = 0.6f),
            topLeft = Offset(size.width - 1.5.dp.toPx(), size.height * 0.3f),
            size = Size(1.5.dp.toPx(), size.height * 0.4f),
            cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
        )
    }
}

private fun getFormattedTime(): String {
    val sdf = SimpleDateFormat("h:mm", Locale.getDefault())
    return sdf.format(Date())
}
