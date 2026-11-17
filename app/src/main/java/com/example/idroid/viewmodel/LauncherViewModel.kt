package com.example.idroid.viewmodel

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.idroid.model.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val _installedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val installedApps: StateFlow<List<AppInfo>> = _installedApps.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isControlCenterOpen = MutableStateFlow(false)
    val isControlCenterOpen: StateFlow<Boolean> = _isControlCenterOpen.asStateFlow()

    init {
        loadInstalledApps()
    }

    fun toggleControlCenter(open: Boolean) {
        _isControlCenterOpen.value = open
    }

    fun loadInstalledApps() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val pm = getApplication<Application>().packageManager
            val intent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val resolveInfos = pm.queryIntentActivities(intent, 0)
            val appList = resolveInfos.mapNotNull { resolveInfo ->
                val label = resolveInfo.loadLabel(pm).toString()
                val packageName = resolveInfo.activityInfo.packageName
                // Don't include ourselves in the grid if needed, or include
                val launchIntent = pm.getLaunchIntentForPackage(packageName)
                val iconDrawable = resolveInfo.loadIcon(pm)
                val bitmap = drawableToBitmap(iconDrawable)
                val imageBitmap = bitmap?.asImageBitmap()

                if (launchIntent != null) {
                    AppInfo(
                        label = label,
                        packageName = packageName,
                        icon = imageBitmap,
                        launchIntent = launchIntent
                    )
                } else null
            }.sortedBy { it.label.lowercase() }

            _installedApps.value = appList
            _isLoading.value = false
        }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            return null
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}
