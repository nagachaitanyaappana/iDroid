package com.example.idroid.util

import androidx.compose.ui.graphics.Color

object IosIconPack {

    data class IosAppBadge(
        val categoryName: String,
        val backgroundColor: Color,
        val symbolLabel: String
    )

    private val systemPackageMap = mapOf(
        "com.google.android.dialer" to IosAppBadge("Phone", Color(0xFF34C759), "📞"),
        "com.android.dialer" to IosAppBadge("Phone", Color(0xFF34C759), "📞"),
        "com.samsung.android.dialer" to IosAppBadge("Phone", Color(0xFF34C759), "📞"),

        "com.google.android.apps.messaging" to IosAppBadge("Messages", Color(0xFF34C759), "💬"),
        "com.android.mms" to IosAppBadge("Messages", Color(0xFF34C759), "💬"),

        "com.android.chrome" to IosAppBadge("Safari", Color(0xFF007AFF), "🧭"),
        "org.mozilla.firefox" to IosAppBadge("Safari", Color(0xFF007AFF), "🧭"),

        "com.google.android.camera" to IosAppBadge("Camera", Color(0xFF8E8E93), "📷"),
        "com.android.camera" to IosAppBadge("Camera", Color(0xFF8E8E93), "📷"),

        "com.android.settings" to IosAppBadge("Settings", Color(0xFF8E8E93), "⚙️"),

        "com.google.android.apps.maps" to IosAppBadge("Maps", Color(0xFF34C759), "🗺️"),

        "com.google.android.youtube" to IosAppBadge("YouTube", Color(0xFFFF3B30), "▶️"),

        "com.whatsapp" to IosAppBadge("WhatsApp", Color(0xFF34C759), "🟢"),

        "com.spotify.music" to IosAppBadge("Spotify", Color(0xFF1DB954), "🎵")
    )

    fun getIosBadgeForPackage(packageName: String): IosAppBadge? {
        return systemPackageMap[packageName]
    }
}
