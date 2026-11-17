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
            delay(1_000L)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 27.dp, end = 22.dp, top = 7.dp, bottom = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currentTime,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onOpenControlCenter)
                .padding(horizontal = 3.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IosSignalIcon()
            IosWifiIcon()
            IosBatteryIcon()
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
        listOf(4.dp, 6.dp, 9.dp, 12.dp).forEach { barHeight ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(barHeight)
                    .clip(RoundedCornerShape(1.dp))
                    .background(Color.White)
            )
        }
    }
}

@Composable
private fun IosWifiIcon() {
    Canvas(modifier = Modifier.size(15.dp)) {
        val stroke = 1.8.dp.toPx()
        drawArc(
            color = Color.White,
            startAngle = 220f,
            sweepAngle = 100f,
            useCenter = false,
            topLeft = Offset.Zero,
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
    Canvas(modifier = Modifier.size(width = 23.dp, height = 12.dp)) {
        val corner = 3.dp.toPx()
        drawRoundRect(
            color = Color.White.copy(alpha = 0.72f),
            cornerRadius = CornerRadius(corner, corner),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.4.dp.toPx())
        )
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(2.dp.toPx(), 2.dp.toPx()),
            size = Size((size.width - 6.dp.toPx()) * 0.82f, size.height - 4.dp.toPx()),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )
        drawRoundRect(
            color = Color.White.copy(alpha = 0.72f),
            topLeft = Offset(size.width - 1.5.dp.toPx(), size.height * 0.3f),
            size = Size(1.5.dp.toPx(), size.height * 0.4f),
            cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
        )
    }
}

private fun getFormattedTime(): String =
    SimpleDateFormat("h:mm", Locale.getDefault()).format(Date())
