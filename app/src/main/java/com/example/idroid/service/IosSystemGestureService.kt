package com.example.idroid.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.idroid.ui.components.IosControlCenter
import com.example.idroid.ui.components.IosLockScreen
import com.example.idroid.ui.theme.IDroidTheme

class IosSystemGestureService : Service(), LifecycleOwner, SavedStateRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    private var windowManager: WindowManager? = null
    private var topEdgeTriggerView: View? = null
    private var fullOverlayView: View? = null

    private var isControlCenterOpen = false
    private var isLockScreenOpen = false

    private val screenStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_SCREEN_OFF -> {
                    isLockScreenOpen = true
                }
                Intent.ACTION_SCREEN_ON -> {
                    if (isLockScreenOpen) {
                        showLockScreenOverlay()
                    }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        startForegroundNotification()

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        registerReceiver(screenStateReceiver, filter)

        setupTopSwipeGestureTrigger()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        if (intent?.action == ACTION_STOP) {
            stopSelf()
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForegroundNotification() {
        val channelId = "ios_system_integration"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "iOS System Integration Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("iOS 18 System Active")
            .setContentText("Top-swipe for Control Center, Screen On for iOS Lock Screen")
            .setSmallIcon(android.R.drawable.sym_def_app_icon)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(1002, notification)
    }

    private fun setupTopSwipeGestureTrigger() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // Thin edge bar at top-right (swipe down to trigger iOS Control Center)
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            40, // thin top gesture strip
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP
        }

        val triggerView = View(this).apply {
            setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                    if (!isControlCenterOpen) {
                        showControlCenterOverlay()
                    }
                }
                false
            }
        }

        topEdgeTriggerView = triggerView
        windowManager?.addView(topEdgeTriggerView, params)
    }

    private fun showControlCenterOverlay() {
        isControlCenterOpen = true
        removeFullOverlay()

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )

        val composeView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@IosSystemGestureService)
            setViewTreeSavedStateRegistryOwner(this@IosSystemGestureService)

            setContent {
                IDroidTheme {
                    IosControlCenter(
                        isOpen = isControlCenterOpen,
                        onClose = {
                            isControlCenterOpen = false
                            removeFullOverlay()
                        }
                    )
                }
            }
        }

        fullOverlayView = composeView
        windowManager?.addView(fullOverlayView, params)
    }

    private fun showLockScreenOverlay() {
        removeFullOverlay()

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
            PixelFormat.TRANSLUCENT
        )

        val composeView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@IosSystemGestureService)
            setViewTreeSavedStateRegistryOwner(this@IosSystemGestureService)

            setContent {
                IDroidTheme {
                    IosLockScreen(
                        isLocked = isLockScreenOpen,
                        onUnlock = {
                            isLockScreenOpen = false
                            removeFullOverlay()
                        }
                    )
                }
            }
        }

        fullOverlayView = composeView
        windowManager?.addView(fullOverlayView, params)
    }

    private fun removeFullOverlay() {
        if (fullOverlayView != null) {
            windowManager?.removeView(fullOverlayView)
            fullOverlayView = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(screenStateReceiver)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        if (topEdgeTriggerView != null) {
            windowManager?.removeView(topEdgeTriggerView)
            topEdgeTriggerView = null
        }
        removeFullOverlay()
    }

    companion object {
        const val ACTION_STOP = "com.example.idroid.service.STOP_SYSTEM_GESTURES"

        fun start(context: Context) {
            val intent = Intent(context, IosSystemGestureService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, IosSystemGestureService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }
}
