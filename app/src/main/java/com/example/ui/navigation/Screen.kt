package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.TheaterComedy
import androidx.compose.material.icons.outlined.Translate
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
    object Roleplay : BottomTab("roleplay", "Roleplay", Icons.Filled.TheaterComedy, Icons.Outlined.TheaterComedy)
    object AiTutor : BottomTab("tutor", "AI Tutor", Icons.Filled.Psychology, Icons.Outlined.Psychology)
    object History : BottomTab("history", "History", Icons.Filled.History, Icons.Outlined.History)
    object Dictionary : BottomTab("dictionary", "Dictionary", Icons.Filled.MenuBook, Icons.Outlined.MenuBook)
    object Profile : BottomTab("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)

    companion object {
        val tabs: List<BottomTab> get() = listOf(Home, Roleplay, AiTutor, History, Dictionary, Profile)
    }
}
