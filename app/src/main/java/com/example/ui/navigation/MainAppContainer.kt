package com.example.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.dictionary.DictionaryScreen
import com.example.ui.dictionary.DictionaryViewModel
import com.example.ui.history.HistoryScreen
import com.example.ui.history.HistoryViewModel
import com.example.ui.home.HomeScreen
import com.example.ui.home.HomeViewModel
import com.example.ui.profile.ProfileScreen
import com.example.ui.profile.ProfileViewModel
import com.example.ui.splash.SplashScreen
import com.example.ui.tutor.AiTutorScreen
import com.example.ui.tutor.AiTutorViewModel

@Composable
fun MainAppContainer(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    var showSplash by remember { mutableStateOf(true) }
    var currentTab by remember { mutableStateOf<BottomTab>(BottomTab.Home) }

    val homeViewModel: HomeViewModel = viewModel()
    val historyViewModel: HistoryViewModel = viewModel()
    val tutorViewModel: AiTutorViewModel = viewModel()
    val dictionaryViewModel: DictionaryViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    if (showSplash) {
        SplashScreen(
            onSplashFinished = { showSplash = false }
        )
    } else {
        Scaffold(
            topBar = {
                DeveloperHeader()
            },
            bottomBar = {
                BottomNavBar(
                    currentTab = currentTab,
                    onTabSelected = { currentTab = it }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                    },
                    label = "tab_crossfade"
                ) { tab ->
                    when (tab) {
                        BottomTab.Home -> HomeScreen(
                            viewModel = homeViewModel,
                            onNavigateToTab = { newTab -> currentTab = newTab },
                            isDarkTheme = isDarkTheme,
                            onToggleTheme = onToggleTheme
                        )
                        BottomTab.History -> HistoryScreen(
                            viewModel = historyViewModel
                        )
                        BottomTab.AiTutor -> AiTutorScreen(
                            viewModel = tutorViewModel
                        )
                        BottomTab.Dictionary -> DictionaryScreen(
                            viewModel = dictionaryViewModel
                        )
                        BottomTab.Profile -> ProfileScreen(
                            viewModel = profileViewModel,
                            isDarkTheme = isDarkTheme,
                            onToggleTheme = onToggleTheme
                        )
                    }
                }
            }
        }
    }
}
