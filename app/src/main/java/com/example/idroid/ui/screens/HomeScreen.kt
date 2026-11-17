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
import androidx.compose.foundation.layout.width
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
import com.example.idroid.ui.components.DynamicIsland
import com.example.idroid.ui.components.IosAppIcon
import com.example.idroid.ui.components.IosAppLibrary
import com.example.idroid.ui.components.IosControlCenter
import com.example.idroid.ui.components.IosDock
import com.example.idroid.ui.components.IosLockScreen
import com.example.idroid.ui.components.IosStatusHeader
import com.example.idroid.ui.components.IosWidgetSection
import com.example.idroid.ui.components.SpotlightSearch
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

    var isLocked by remember { mutableStateOf(false) }
    var isSpotlightOpen by remember { mutableStateOf(false) }
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
            // iOS Status Bar & Dynamic Island Header
            Column {
                IosStatusHeader(
                    onOpenControlCenter = { viewModel.toggleControlCenter(true) }
                )
                DynamicIsland()
            }

            // Quick Theme & Lock triggers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.25f))
                        .clickable { isLocked = true }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("🔒 Lock Screen", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.25f))
                        .clickable { showThemeSettings = true }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("⚙️ Theme Options", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }

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
                // App pages + 1 App Library Page
                val pageCount = (if (pages.isEmpty()) 1 else pages.size) + 1
                val pagerState = rememberPagerState(pageCount = { pageCount })

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f)
                    ) { page ->
                        if (page == pageCount - 1) {
                            // Final Page: iOS App Library
                            IosAppLibrary(
                                apps = installedApps,
                                onAppClick = { app -> launchApp(context, app) }
                            )
                        } else {
                            Column(modifier = Modifier.fillMaxSize()) {
                                if (page == 0) {
                                    // First Page shows iOS Widgets
                                    IosWidgetSection()
                                    Spacer(modifier = Modifier.height(8.dp))
                                }

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
                        }
                    }

                    // Pager Indicator & Spotlight Search Button
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
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

                        Spacer(modifier = Modifier.width(12.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.25f))
                                .clickable { isSpotlightOpen = true }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("🔍 Search", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
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

        // Spotlight Search Sheet
        SpotlightSearch(
            isOpen = isSpotlightOpen,
            apps = installedApps,
            onAppClick = { app -> launchApp(context, app) },
            onClose = { isSpotlightOpen = false }
        )

        // Control Center Overlay
        IosControlCenter(
            isOpen = isControlCenterOpen,
            onClose = { viewModel.toggleControlCenter(false) }
        )

        // iOS Lock Screen
        IosLockScreen(
            isLocked = isLocked,
            onUnlock = { isLocked = false }
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
