package com.example.idroid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.idroid.model.AppInfo

@Composable
fun IosDock(
    dockApps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White.copy(alpha = 0.28f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.4f),
                shape = RoundedCornerShape(32.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            dockApps.take(4).forEach { app ->
                IosAppIcon(
                    label = app.label,
                    icon = app.icon,
                    onClick = { onAppClick(app) },
                    showLabel = false,
                    iconSize = 56.dp
                )
            }
        }
    }
}
