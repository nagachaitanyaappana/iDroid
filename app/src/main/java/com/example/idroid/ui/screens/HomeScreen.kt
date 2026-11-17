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
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
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
import com.example.idroid.ui.components.IosAppLibrary
import com.example.idroid.ui.components.IosControlCenter
import com.example.idroid.ui.components.IosDock
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

    var isSpotlightOpen by remember { mutableStateOf(false) }

    // A launcher should not display its own app as an icon.
    val homeApps = remember(installedApps, context.packageName) {
        installedApps.filterNot { it.packageName == context.packageName }
    }

    // Keep familiar communication and camera apps in the iOS-style dock.
    val dockApps = remember(homeApps) {
        val selected = mutableListOf<AppInfo>()
        val preferredGroups = listOf(
            listOf("phone", "dialer"),
            listOf("message", "sms"),
            listOf("camera"),
            listOf("chrome", "browser")
        )
        preferredGroups.forEach { group ->
            homeApps.firstOrNull { app ->
                app !in selected && group.any { keyword ->
                    app.label.contains(keyword, ignoreCase = true) ||
                        app.packageName.contains(keyword, ignoreCase = true)
                }
            }?.let(selected::add)
        }
        if (selected.size < 4) {
            selected += homeApps.filterNot { it in selected }.take(4 - selected.size)
        }
        selected
    }

    // A layered, blurred-colour composition inspired by the depth of iOS wallpapers.
    val wallpaper = Brush.linearGradient(
        colors = listOf(
            Color(0xFF101B3B),
            Color(0xFF273D72),
            Color(0xFF624B83),
            Color(0xFFB66A83)
        ),
        start = androidx.compose.ui.geometry.Offset.Zero,
        end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(wallpaper)
    ) {
        // Layered colour blooms give the wallpaper a soft, photographic depth.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF79C9D2).copy(alpha = 0.58f), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(180f, 380f),
                        radius = 850f
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFEF9BA8).copy(alpha = 0.58f), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(900f, 1500f),
                        radius = 1050f
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF344C9A).copy(alpha = 0.48f), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(850f, 420f),
                        radius = 900f
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {
            IosStatusHeader(
                onOpenControlCenter = { viewModel.toggleControlCenter(true) }
            )

            if (isLoading) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                val appsPerPage = 20
                val appPages = homeApps.chunked(appsPerPage)
                val appPageCount = appPages.size.coerceAtLeast(1)
                val pageCount = appPageCount + 1
                val pagerState = rememberPagerState(pageCount = { pageCount })

                Column(modifier = Modifier.weight(1f)) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f),
                        key = { it }
                    ) { page ->
                        if (page == appPageCount) {
                            IosAppLibrary(
                                apps = homeApps,
                                onAppClick = { app -> launchApp(context, app) }
                            )
                        } else {
                            Column(modifier = Modifier.fillMaxSize()) {
                                if (page == 0) {
                                    IosWidgetSection()
                                }

                                val pageApps = appPages.getOrNull(page).orEmpty()
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(4),
                                    contentPadding = PaddingValues(
                                        start = 12.dp,
                                        end = 12.dp,
                                        top = 8.dp,
                                        bottom = 14.dp
                                    ),
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    verticalArrangement = Arrangement.spacedBy(15.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(
                                        items = pageApps,
                                        key = { it.packageName }
                                    ) { app ->
                                        IosAppIcon(
                                            label = app.label,
                                            icon = app.icon,
                                            onClick = { launchApp(context, app) },
                                            modifier = Modifier.fillMaxWidth(),
                                            iconSize = 58.dp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // iOS page dots and the subtle Search affordance.
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(pageCount) { index ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp)
                                    .size(if (pagerState.currentPage == index) 7.dp else 6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (pagerState.currentPage == index) Color.White
                                        else Color.White.copy(alpha = 0.42f)
                                    )
                            )
                        }

                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "⌕",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 19.sp,
                            modifier = Modifier.clickable { isSpotlightOpen = true }
                        )
                    }
                }
            }

            IosDock(
                dockApps = dockApps,
                onAppClick = { app -> launchApp(context, app) }
            )
        }

        SpotlightSearch(
            isOpen = isSpotlightOpen,
            apps = homeApps,
            onAppClick = { app -> launchApp(context, app) },
            onClose = { isSpotlightOpen = false }
        )

        IosControlCenter(
            isOpen = isControlCenterOpen,
            onClose = { viewModel.toggleControlCenter(false) }
        )
    }
}

private fun launchApp(context: Context, app: AppInfo) {
    val launchIntent = app.launchIntent ?: return
    try {
        launchIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(launchIntent)
    } catch (e: Exception) {
        Toast.makeText(context, "Unable to launch ${app.label}", Toast.LENGTH_SHORT).show()
    }
}
