package com.vourourou.forklife.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.vourourou.forklife.R
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vourourou.forklife.ui.components.ForkLifeBackTopAppBar
import com.vourourou.forklife.ui.components.ForkLifeBottomNavigation
import com.vourourou.forklife.ui.components.ForkLifeExtendedFAB
import com.vourourou.forklife.ui.components.ForkLifeTopAppBar
import com.vourourou.forklife.ui.components.shouldShowBottomBar
import com.vourourou.forklife.ui.navigation.ForkLifeNavHost
import com.vourourou.forklife.ui.navigation.Screen
import com.vourourou.forklife.ui.theme.ForkLifeTheme
import com.vourourou.forklife.ui.theme.toDarkModePreference
import com.vourourou.forklife.ui.theme.toForkLifeColorTheme
import com.vourourou.forklife.utils.DataStoreManager

@Composable
fun ForkLifeApp(
    dataStoreManager: DataStoreManager,
    onReady: () -> Unit = {}
) {
    val navController = rememberNavController()

    val theme by dataStoreManager.themeFlow.collectAsState(initial = null)
    val darkMode by dataStoreManager.darkModeFlow.collectAsState(initial = null)

    // Only render when DataStore values are loaded to avoid recomposition jump
    val currentTheme = theme
    val currentDarkMode = darkMode

    // Signal ready when DataStore values are loaded
    LaunchedEffect(currentTheme, currentDarkMode) {
        if (currentTheme != null && currentDarkMode != null) {
            onReady()
        }
    }

    if (currentTheme != null && currentDarkMode != null) {
        ForkLifeTheme(
            colorTheme = currentTheme.toForkLifeColorTheme(),
            darkModePreference = currentDarkMode.toDarkModePreference()
        ) {
            ForkLifeScaffold(
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ForkLifeScaffold(
    navController: NavHostController
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val showBottomBar = shouldShowBottomBar(navController)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            ForkLifeTopBar(
                currentRoute = currentRoute,
                navController = navController
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                ForkLifeBottomNavigation(navController = navController)
            }
        },
        floatingActionButton = {
            if (currentRoute == Screen.Home.route) {
                ForkLifeExtendedFAB(
                    text = stringResource(R.string.scanner),
                    icon = Icons.Default.CameraAlt,
                    onClick = { navController.navigate(Screen.Scan.route) }
                )
            }
        }
    ) { paddingValues ->
        ForkLifeNavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            paddingValues = paddingValues
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ForkLifeTopBar(
    currentRoute: String?,
    navController: NavHostController
) {
    when {
        currentRoute == Screen.Home.route -> {
            ForkLifeTopAppBar(
                title = stringResource(R.string.app_name),
                windowInsets = TopAppBarDefaults.windowInsets,
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings)
                        )
                    }
                }
            )
        }

        currentRoute == Screen.History.route -> {
            ForkLifeTopAppBar(
                title = stringResource(R.string.history),
                windowInsets = TopAppBarDefaults.windowInsets,
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings)
                        )
                    }
                }
            )
        }

        currentRoute == Screen.Stats.route -> {
            ForkLifeTopAppBar(
                title = stringResource(R.string.statistics),
                windowInsets = TopAppBarDefaults.windowInsets,
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings)
                        )
                    }
                }
            )
        }

        currentRoute == Screen.Settings.route -> {
            ForkLifeBackTopAppBar(
                title = stringResource(R.string.settings),
                onBackClick = { navController.popBackStack() },
                windowInsets = TopAppBarDefaults.windowInsets
            )
        }

        currentRoute?.startsWith("manual_scan") == true -> {
            ForkLifeBackTopAppBar(
                title = stringResource(R.string.manual_entry),
                onBackClick = { navController.popBackStack() },
                windowInsets = TopAppBarDefaults.windowInsets
            )
        }

        currentRoute == Screen.Scan.route -> {
            // No top bar for scan screen - it has its own overlay
        }
    }
}
