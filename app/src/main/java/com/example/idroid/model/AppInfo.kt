package com.example.idroid.model

import android.content.Intent
import androidx.compose.ui.graphics.ImageBitmap

data class AppInfo(
    val label: String,
    val packageName: String,
    val icon: ImageBitmap?,
    val launchIntent: Intent?
)
