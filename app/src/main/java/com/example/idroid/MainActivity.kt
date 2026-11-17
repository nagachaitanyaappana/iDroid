package com.example.idroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.idroid.ui.screens.HomeScreen
import com.example.idroid.ui.theme.IDroidTheme
import com.example.idroid.viewmodel.LauncherViewModel

class MainActivity : ComponentActivity() {

    private val launcherViewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IDroidTheme {
                HomeScreen(
                    viewModel = launcherViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
