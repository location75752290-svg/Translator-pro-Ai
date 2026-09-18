package com.example

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.example.ui.navigation.MainAppContainer
import com.example.ui.theme.TranslatorProTheme
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                MobileAds.initialize(this@MainActivity) {}
            } catch (e: Exception) {
                Log.w("MainActivity", "MobileAds initialization skipped: ${e.message}")
            }
        }

        enableEdgeToEdge()
        setContent {
            val systemInDark = isSystemInDarkTheme()
            var isDarkTheme by rememberSaveable { mutableStateOf(systemInDark) }

            TranslatorProTheme(darkTheme = isDarkTheme) {
                MainAppContainer(
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }
}
