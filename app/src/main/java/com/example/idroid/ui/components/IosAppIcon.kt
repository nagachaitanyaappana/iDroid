package com.example.idroid.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IosAppIcon(
    label: String,
    icon: ImageBitmap?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 60.dp,
    showLabel: Boolean = true
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(iconSize + 16.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(iconSize)
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.9f)),
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) {
                Image(
                    bitmap = icon,
                    contentDescription = label,
                    modifier = Modifier.size(iconSize * 0.85f)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(iconSize)
                        .background(Color(0xFF007AFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label.take(1).uppercase(),
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (showLabel) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}
