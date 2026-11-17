package com.example.idroid.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idroid.model.AppInfo
import com.example.idroid.ui.components.IosAppIcon
import com.example.idroid.ui.components.IosControlCenter
import com.example.idroid.ui.components.IosDock
import com.example.idroid.ui.components.IosStatusHeader
import com.example.idroid.ui.components.IosWidgetSection
import com.example.idroid.viewmodel.LauncherViewModel

@Composable
fun HomeScreen(
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val installedApps by viewModel.installedApps.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isControlCenterOpen by viewModel.isControlCenterOpen.collectAsState()
    var showThemeSettings by remember { mutableStateOf(false) }

    if (showThemeSettings) {
        ThemeSettingsScreen(
            onBackToLauncher = { showThemeSettings = false },
            modifier = modifier
        )
        return
    }

    // iOS wallpaper gradient
    val wallpaperGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(wallpaperGradient)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // iOS Status Header
            IosStatusHeader(
                onOpenControlCenter = { viewModel.toggleControlCenter(true) }
            )

            // Theme Settings Trigger Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.25f))
                        .clickable { showThemeSettings = true }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "⚙️ Theme Options",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // iOS Widgets
            IosWidgetSection()

            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                val appsPerPage = 16
                val pages = installedApps.chunked(appsPerPage)
                val pageCount = if (pages.isEmpty()) 1 else pages.size
                val pagerState = rememberPagerState(pageCount = { pageCount })

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f)
                    ) { page ->
                        val pageApps = pages.getOrNull(page) ?: emptyList()
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(pageApps) { app ->
                                IosAppIcon(
                                    label = app.label,
                                    icon = app.icon,
                                    onClick = { launchApp(context, app) }
                                )
                            }
                        }
                    }

                    // Pager Indicator Dots
                    if (pageCount > 1) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            repeat(pageCount) { index ->
                                val color = if (pagerState.currentPage == index) {
                                    Color.White
                                } else {
                                    Color.White.copy(alpha = 0.35f)
                                }
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                )
                            }
                        }
                    }
                }
            }

            // iOS Dock
            IosDock(
                dockApps = installedApps,
                onAppClick = { app -> launchApp(context, app) }
            )
        }

        // Control Center Overlay
        IosControlCenter(
            isOpen = isControlCenterOpen,
            onClose = { viewModel.toggleControlCenter(false) }
        )
    }
}

private fun launchApp(context: Context, app: AppInfo) {
    if (app.launchIntent != null) {
        try {
            context.startActivity(app.launchIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to launch ${app.label}", Toast.LENGTH_SHORT).show()
        }
    }
}
