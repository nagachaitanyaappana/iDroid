package com.example.idroid.ui.components

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.idroid.model.AppInfo

@Composable
fun IosAppLibrary(
    apps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    val socialKeywords = listOf("chat", "message", "social", "whatsapp", "facebook", "twitter", "instagram", "tele")
    val mediaKeywords = listOf("music", "video", "play", "youtube", "spotify", "media", "camera", "photo")
    val utilityKeywords = listOf("settings", "chrome", "browse", "file", "clock", "calculator", "mail", "tool")

    val socialApps = apps.filter { app ->
        socialKeywords.any { kw -> app.label.contains(kw, ignoreCase = true) || app.packageName.contains(kw, ignoreCase = true) }
    }
    val mediaApps = apps.filter { app ->
        mediaKeywords.any { kw -> app.label.contains(kw, ignoreCase = true) || app.packageName.contains(kw, ignoreCase = true) }
    }
    val utilityApps = apps.filter { app ->
        utilityKeywords.any { kw -> app.label.contains(kw, ignoreCase = true) || app.packageName.contains(kw, ignoreCase = true) }
    }
    val otherApps = apps.filter { it !in socialApps && it !in mediaApps && it !in utilityApps }

    val categories = listOf(
        "Social" to socialApps.ifEmpty { apps.take(4) },
        "Media & Entertainment" to mediaApps.ifEmpty { apps.drop(4).take(4) },
        "Utilities" to utilityApps.ifEmpty { apps.drop(8).take(4) },
        "Other" to otherApps.ifEmpty { apps.drop(12).take(4) }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "App Library",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(categories) { (categoryTitle, categoryApps) ->
                AppLibraryFolderCard(
                    title = categoryTitle,
                    apps = categoryApps,
                    onAppClick = onAppClick
                )
            }
        }
    }
}

@Composable
private fun AppLibraryFolderCard(
    title: String,
    apps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White.copy(alpha = 0.22f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.35f),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(apps.take(4)) { app ->
                    IosAppIcon(
                        label = app.label,
                        icon = app.icon,
                        onClick = { onAppClick(app) },
                        showLabel = false,
                        iconSize = 44.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
