package com.example.idroid.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun IosLockScreen(
    isLocked: Boolean,
    onUnlock: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTime by remember { mutableStateOf(getFormattedTime()) }
    var currentDate by remember { mutableStateOf(getFormattedDate()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = getFormattedTime()
            currentDate = getFormattedDate()
            delay(1000L)
        }
    }

    AnimatedVisibility(
        visible = isLocked,
        exit = fadeOut() + slideOutVertically { -it }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1C1C1E),
                            Color(0xFF0F2027),
                            Color(0xFF203A43)
                        )
                    )
                )
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (dragAmount < -30) {
                            onUnlock()
                        }
                    }
                }
                .clickable { onUnlock() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top: Lock Icon & Date & Big Clock
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🔒",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = currentDate,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = currentTime,
                        color = Color.White,
                        fontSize = 84.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-2).sp
                    )
                }

                // Middle: Sample iOS Notification
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color.White.copy(alpha = 0.18f))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "💬 MESSAGES",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "now",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 11.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "iDroid iOS 18 Theme",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Swipe up or tap anywhere to unlock your phone.",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.sp
                        )
                    }
                }

                // Bottom: Torch, Swipe Bar, Camera
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Flashlight Button
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🔦", fontSize = 24.sp)
                        }

                        // Camera Button
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📷", fontSize = 24.sp)
                        }
                    }

                    // Swipe Home Indicator Bar
                    Box(
                        modifier = Modifier
                            .width(140.dp)
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Swipe up to open",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

private fun getFormattedTime(): String {
    val sdf = SimpleDateFormat("h:mm", Locale.getDefault())
    return sdf.format(Date())
}

private fun getFormattedDate(): String {
    val sdf = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
    return sdf.format(Date())
}
