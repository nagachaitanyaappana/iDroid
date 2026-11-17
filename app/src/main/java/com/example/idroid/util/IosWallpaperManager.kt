package com.example.idroid.util

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.os.Build

object IosWallpaperManager {

    enum class IosWallpaperPreset(
        val title: String,
        val topColor: Long,
        val bottomColor: Long
    ) {
        IOS_18_DARK("iOS 18 Dark", 0xFF0F2027, 0xFF2C5364),
        IOS_18_LIGHT("iOS 18 Light", 0xFF007AFF, 0xFF5856D6),
        IOS_SUNSET("iOS Sunset", 0xFFFF9500, 0xFFFF2D55),
        IOS_MIDNIGHT("iOS Midnight", 0xFF1C1C1E, 0xFF000000)
    }

    fun applyWallpaper(
        context: Context,
        preset: IosWallpaperPreset,
        applyToLockScreen: Boolean = true
    ): Boolean {
        return try {
            val width = 1080
            val height = 2400
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            val paint = Paint().apply {
                shader = LinearGradient(
                    0f, 0f, 0f, height.toFloat(),
                    preset.topColor.toInt(),
                    preset.bottomColor.toInt(),
                    Shader.TileMode.CLAMP
                )
            }
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

            val wallpaperManager = WallpaperManager.getInstance(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
                if (applyToLockScreen) {
                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                }
            } else {
                wallpaperManager.setBitmap(bitmap)
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}
