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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idroid.model.AppInfo

@Composable
fun SpotlightSearch(
    isOpen: Boolean,
    apps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredApps = if (searchQuery.isBlank()) {
        apps.take(8)
    } else {
        apps.filter { it.label.contains(searchQuery, ignoreCase = true) }
    }

    AnimatedVisibility(
        visible = isOpen,
        enter = fadeIn() + slideInVertically { -it },
        exit = fadeOut() + slideOutVertically { -it }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.75f))
                .clickable { onClose() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, start = 16.dp, end = 16.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFF1C1C1E).copy(alpha = 0.95f))
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .clickable(enabled = false) {}
                    .padding(16.dp)
            ) {
                // Search Input Bar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search Apps...", color = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF007AFF),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                    )

                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable { onClose() }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text("Cancel", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (searchQuery.isBlank()) "Top Hits" else "Search Results",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.height(280.dp)
                ) {
                    items(filteredApps) { app ->
                        IosAppIcon(
                            label = app.label,
                            icon = app.icon,
                            onClick = {
                                onAppClick(app)
                                onClose()
                            }
                        )
                    }
                }
            }
        }
    }
}
