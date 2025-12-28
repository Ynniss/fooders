package com.vourourou.forklife.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vourourou.forklife.ui.components.ForkLifeBackTopAppBar
import com.vourourou.forklife.ui.components.ForkLifeBottomNavigation
import com.vourourou.forklife.ui.components.ForkLifeExtendedFAB
import com.vourourou.forklife.ui.components.ForkLifeMediumTopAppBar
import com.vourourou.forklife.ui.components.ForkLifeTopAppBar
import com.vourourou.forklife.ui.components.shouldShowBottomBar
import com.vourourou.forklife.ui.navigation.ForkLifeNavHost
import com.vourourou.forklife.ui.navigation.Screen
import com.vourourou.forklife.ui.theme.ForkLifeTheme
import com.vourourou.forklife.ui.theme.toDarkModePreference
import com.vourourou.forklife.ui.theme.toForkLifeColorTheme
import com.vourourou.forklife.utils.DataStoreManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForkLifeApp(
    dataStoreManager: DataStoreManager
) {
    val navController = rememberNavController()

    val theme by dataStoreManager.themeFlow.collectAsState(initial = "Orange")
    val darkMode by dataStoreManager.darkModeFlow.collectAsState(initial = "System")

    ForkLifeTheme(
        colorTheme = theme.toForkLifeColorTheme(),
        darkModePreference = darkMode.toDarkModePreference()
    ) {
        ForkLifeScaffold(
            navController = navController
        )
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

    // Create scroll behaviors - remembered per route type
    val historyScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val profileScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Determine current scroll behavior based on route
    val currentScrollBehavior: TopAppBarScrollBehavior? = when {
        currentRoute == Screen.History.route -> historyScrollBehavior
        currentRoute == Screen.Profile.route -> profileScrollBehavior
        else -> null
    }

    val scaffoldModifier = if (currentScrollBehavior != null) {
        Modifier
            .fillMaxSize()
            .nestedScroll(currentScrollBehavior.nestedScrollConnection)
    } else {
        Modifier.fillMaxSize()
    }

    Scaffold(
        modifier = scaffoldModifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            ForkLifeTopBar(
                currentRoute = currentRoute,
                navController = navController,
                historyScrollBehavior = historyScrollBehavior,
                profileScrollBehavior = profileScrollBehavior
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
                    text = "Scanner",
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
    navController: NavHostController,
    historyScrollBehavior: TopAppBarScrollBehavior,
    profileScrollBehavior: TopAppBarScrollBehavior
) {
    when {
        currentRoute == Screen.Home.route -> {
            ForkLifeTopAppBar(
                title = "ForkLife",
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        }
        currentRoute == Screen.History.route -> {
            ForkLifeMediumTopAppBar(
                title = "Historique",
                scrollBehavior = historyScrollBehavior,
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        }
        currentRoute == Screen.Profile.route -> {
            ForkLifeMediumTopAppBar(
                title = "Profil",
                scrollBehavior = profileScrollBehavior,
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Parametres"
                        )
                    }
                }
            )
        }
        currentRoute == Screen.Settings.route -> {
            ForkLifeBackTopAppBar(
                title = "Parametres",
                onBackClick = { navController.popBackStack() }
            )
        }
        currentRoute?.startsWith("manual_scan") == true -> {
            ForkLifeBackTopAppBar(
                title = "Saisie manuelle",
                onBackClick = { navController.popBackStack() }
            )
        }
        currentRoute == Screen.Scan.route -> {
            // No top bar for scan screen - it has its own overlay
        }
    }
}
