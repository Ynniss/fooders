package com.esgi.fooders.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.esgi.fooders.ui.components.FoodersBottomNavigation
import com.esgi.fooders.ui.components.shouldShowBottomBar
import com.esgi.fooders.ui.navigation.FoodersNavHost
import com.esgi.fooders.ui.navigation.Screen
import com.esgi.fooders.ui.theme.DarkModePreference
import com.esgi.fooders.ui.theme.FoodersColorTheme
import com.esgi.fooders.ui.theme.FoodersTheme
import com.esgi.fooders.ui.theme.toDarkModePreference
import com.esgi.fooders.ui.theme.toFoodersColorTheme
import com.esgi.fooders.utils.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun FoodersApp(
    dataStoreManager: DataStoreManager,
    isLoggedIn: Boolean
) {
    val navController = rememberNavController()

    val theme by dataStoreManager.themeFlow.collectAsState(initial = "Orange")
    val darkMode by dataStoreManager.darkModeFlow.collectAsState(initial = "System")

    FoodersTheme(
        colorTheme = theme.toFoodersColorTheme(),
        darkModePreference = darkMode.toDarkModePreference()
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val showBottomBar = shouldShowBottomBar(navController)

            Scaffold(
                bottomBar = {
                    AnimatedVisibility(
                        visible = showBottomBar,
                        enter = slideInVertically(initialOffsetY = { it }),
                        exit = slideOutVertically(targetOffsetY = { it })
                    ) {
                        FoodersBottomNavigation(navController = navController)
                    }
                }
            ) { paddingValues ->
                FoodersNavHost(
                    navController = navController,
                    startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}
