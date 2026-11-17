package com.example.idroid.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.idroid.service.IosSystemGestureService
import com.example.idroid.util.IosWallpaperManager

@Composable
fun ThemeSettingsScreen(
    onBackToLauncher: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isSystemGestureEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F2027))
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "iOS 18 System Integration",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .clickable { onBackToLauncher() }
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

        Spacer(modifier = Modifier.height(24.dp))

        // Section 1: Replace ColorOS Lock Screen & Control Center
        ThemeSectionCard(title = "Unified iOS Lock & Control Center") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Intercept Top Swipe & Lock Screen",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Prevents ColorOS/iDroid clash. Top swipe opens ONLY iOS Control Center, Screen On shows ONLY iOS Lock Screen.",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Switch(
                    checked = isSystemGestureEnabled,
                    onCheckedChange = { checked ->
                        if (checked) {
                            if (!Settings.canDrawOverlays(context)) {
                                val intent = Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:${context.packageName}")
                                )
                                context.startActivity(intent)
                                Toast.makeText(
                                    context,
                                    "Please grant Display Over Other Apps permission",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                IosSystemGestureService.start(context)
                                isSystemGestureEnabled = true
                                Toast.makeText(context, "iOS System Gestures & Lock Active", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            IosSystemGestureService.stop(context)
                            isSystemGestureEnabled = false
                            Toast.makeText(context, "iOS System Gestures Disabled", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF34C759)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section 2: OxygenOS / ColorOS Icon Engine Application
        ThemeSectionCard(title = "OxygenOS / ColorOS Icon Theme") {
            Column {
                Text(
                    text = "Apply iOS continuous squircles and vector icons inside OxygenOS/ColorOS System Settings:",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF34C759))
                        .clickable {
                            try {
                                val intent = Intent("android.settings.PERSONALIZATION_SETTINGS")
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                try {
                                    val intent = Intent(Settings.ACTION_DISPLAY_SETTINGS)
                                    context.startActivity(intent)
                                } catch (ex: Exception) {
                                    Toast.makeText(context, "Open Settings -> Personalization -> Icons", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Open System Icon Customizer",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section 3: Set Phone Wallpapers (Lock & Home Screen)
        ThemeSectionCard(title = "iOS Stock Wallpapers") {
            Column {
                Text(
                    text = "Apply directly to your phone's Lock Screen & Home Screen:",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(14.dp))

                val presets = IosWallpaperManager.IosWallpaperPreset.entries
                presets.forEach { preset ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .clickable {
                                val success = IosWallpaperManager.applyWallpaper(context, preset)
                                if (success) {
                                    Toast.makeText(
                                        context,
                                        "${preset.title} applied to Home & Lock Screen!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(context, "Failed to set wallpaper", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color(preset.topColor))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = preset.title,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = "Apply",
                            color = Color(0xFF007AFF),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}
