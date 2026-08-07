package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Main : Screen("main")
}

sealed class BottomTab(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : BottomTab("home", "Home", Icons.Filled.Translate, Icons.Outlined.Translate)
    object History : BottomTab("history", "History", Icons.Filled.History, Icons.Outlined.History)
    object AiTutor : BottomTab("tutor", "AI Tutor", Icons.Filled.Psychology, Icons.Outlined.Psychology)
    object Dictionary : BottomTab("dictionary", "Dictionary", Icons.Filled.MenuBook, Icons.Outlined.MenuBook)
    object Profile : BottomTab("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)

    companion object {
        val tabs: List<BottomTab> get() = listOf(Home, History, AiTutor, Dictionary, Profile)
    }
}
