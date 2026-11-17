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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Panel = Color(0xFF202126)
private val Tile = Color(0xFF35363D)
private val Blue = Color(0xFF0A84FF)
private val Green = Color(0xFF30D158)

@Composable
fun IosControlCenter(
    isOpen: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isOpen,
        modifier = modifier,
        enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 3 }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 3 })
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.48f))
                .clickable { onClose() }
                .padding(start = 10.dp, end = 10.dp, top = 12.dp, bottom = 18.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(34.dp))
                    .background(Panel.copy(alpha = 0.97f))
                    .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(34.dp))
                    .clickable(enabled = false) {}
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Control Center", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                        Text("iDroid", color = Color.White.copy(alpha = 0.58f), fontSize = 12.sp)
                    }
                    Text(
                        "Done",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.13f))
                            .clickable { onClose() }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ConnectivityCard(Modifier.weight(1.08f))
                    MediaCard(Modifier.weight(0.92f))
                }

                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SliderCard(
                        title = "Brightness",
                        symbol = "☀",
                        modifier = Modifier.weight(1f),
                        initialValue = 0.72f
                    )
                    SliderCard(
                        title = "Volume",
                        symbol = "🔊",
                        modifier = Modifier.weight(1f),
                        initialValue = 0.48f
                    )
                }

                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ActionTile("🔦", "Flashlight", Modifier.weight(1f), Green)
                    ActionTile("◐", "Dark Mode", Modifier.weight(1f), Color(0xFF636366))
                    ActionTile("▣", "Screen", Modifier.weight(1f), Blue)
                    ActionTile("🔒", "Rotation", Modifier.weight(1f), Color(0xFFFF9F0A))
                }

                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ActionTile("⌁", "AirDrop", Modifier.weight(1f), Color(0xFF5E5CE6))
                    ActionTile("⌁", "Focus", Modifier.weight(1f), Color(0xFFBF5AF2))
                    ActionTile("⌂", "Home", Modifier.weight(1f), Color(0xFFFF9F0A))
                    ActionTile("⚙", "Settings", Modifier.weight(1f), Color(0xFF8E8E93))
                }

                Spacer(Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(width = 36.dp, height = 5.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.32f))
                )
            }
        }
    }
}

@Composable
private fun ConnectivityCard(modifier: Modifier = Modifier) {
    var wifi by remember { mutableStateOf(true) }
    var bluetooth by remember { mutableStateOf(true) }
    var mobileData by remember { mutableStateOf(true) }
    var airplane by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .height(164.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Tile)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            ConnectivityButton("◉", "Wi-Fi", wifi, Modifier.weight(1f)) { wifi = !wifi }
            ConnectivityButton("ᛒ", "Bluetooth", bluetooth, Modifier.weight(1f)) { bluetooth = !bluetooth }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            ConnectivityButton("▮", "Mobile", mobileData, Modifier.weight(1f)) { mobileData = !mobileData }
            ConnectivityButton("✈", "Airplane", airplane, Modifier.weight(1f)) { airplane = !airplane }
        }
    }
}

@Composable
private fun ConnectivityButton(
    symbol: String,
    label: String,
    active: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (active) Blue else Color(0xFF55565D))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(symbol, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun MediaCard(modifier: Modifier = Modifier) {
    var playing by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .height(164.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Tile)
            .padding(13.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("♫  NOW PLAYING", color = Color.White.copy(alpha = 0.62f), fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(9.dp))
            Text("No media", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text("Audio will appear here", color = Color.White.copy(alpha = 0.55f), fontSize = 10.sp)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ⅱ", color = Color.White, fontSize = 17.sp, modifier = Modifier.clickable { playing = !playing })
            Text(if (playing) "▶" else "▶", color = Color.White, fontSize = 19.sp)
            Text("»", color = Color.White, fontSize = 19.sp)
        }
    }
}

@Composable
private fun SliderCard(
    title: String,
    symbol: String,
    modifier: Modifier = Modifier,
    initialValue: Float
) {
    var value by remember { mutableFloatStateOf(initialValue) }
    Column(
        modifier = modifier
            .height(112.dp)
            .clip(RoundedCornerShape(23.dp))
            .background(Tile)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(symbol, color = Color.White, fontSize = 15.sp)
            Text(title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
        Slider(
            value = value,
            onValueChange = { value = it },
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.White.copy(alpha = 0.22f)
            )
        )
    }
}

@Composable
private fun ActionTile(
    symbol: String,
    label: String,
    modifier: Modifier = Modifier,
    tint: Color
) {
    Column(
        modifier = modifier
            .height(76.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Tile)
            .clickable { }
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(tint),
            contentAlignment = Alignment.Center
        ) {
            Text(symbol, color = Color.White, fontSize = 15.sp)
        }
        Spacer(Modifier.height(5.dp))
        Text(label, color = Color.White.copy(alpha = 0.88f), fontSize = 9.sp, fontWeight = FontWeight.Medium, maxLines = 1)
    }
}
