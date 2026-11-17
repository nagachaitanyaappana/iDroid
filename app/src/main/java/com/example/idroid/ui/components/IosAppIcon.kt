package com.example.idroid.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
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
    showLabel: Boolean = true,
    badgeCount: Int = 0
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(iconSize + 16.dp)
            .clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Box(
                modifier = Modifier
                    .size(iconSize)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(22.dp),
                        ambientColor = Color.Black.copy(alpha = 0.3f),
                        spotColor = Color.Black.copy(alpha = 0.5f)
                    )
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color.White.copy(alpha = 0.95f)),
                contentAlignment = Alignment.Center
            ) {
                if (icon != null) {
                    Image(
                        bitmap = icon,
                        contentDescription = label,
                        modifier = Modifier.size(iconSize * 0.88f)
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
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // iOS Red Notification Badge
            if (badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF3B30)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (badgeCount > 99) "99+" else badgeCount.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
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
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.6f),
                        offset = Offset(0f, 1f),
                        blurRadius = 3f
                    )
                )
            )
        }
    }
}
