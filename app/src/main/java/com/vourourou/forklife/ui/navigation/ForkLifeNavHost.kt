package com.vourourou.forklife.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vourourou.forklife.ui.history.HistoryScreen
import com.vourourou.forklife.ui.home.HomeScreen
import com.vourourou.forklife.ui.product.ProductDetailScreen
import com.vourourou.forklife.ui.scan.ManualScanScreen
import com.vourourou.forklife.ui.scan.ScanScreen
import com.vourourou.forklife.ui.settings.SettingsScreen
import com.vourourou.forklife.ui.stats.StatsScreen

private const val ANIMATION_DURATION = 300

@Composable
fun ForkLifeNavHost(
    navController: NavHostController,
    startDestination: String,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(ANIMATION_DURATION)
            ) + fadeIn(animationSpec = tween(ANIMATION_DURATION))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(ANIMATION_DURATION)
            ) + fadeOut(animationSpec = tween(ANIMATION_DURATION))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(ANIMATION_DURATION)
            ) + fadeIn(animationSpec = tween(ANIMATION_DURATION))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(ANIMATION_DURATION)
            ) + fadeOut(animationSpec = tween(ANIMATION_DURATION))
        }
    ) {
        // Home Screen
        composable(
            route = Screen.Home.route,
            enterTransition = { fadeIn(animationSpec = tween(ANIMATION_DURATION)) },
            exitTransition = { fadeOut(animationSpec = tween(ANIMATION_DURATION)) },
            popEnterTransition = { fadeIn(animationSpec = tween(ANIMATION_DURATION)) },
            popExitTransition = { fadeOut(animationSpec = tween(ANIMATION_DURATION)) }
        ) {
            HomeScreen(
                paddingValues = paddingValues,
                onNavigateToScan = {
                    navController.navigate(Screen.Scan.route)
                },
                onNavigateToManualScan = {
                    navController.navigate(Screen.ManualScan.createRoute())
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToStats = {
                    navController.navigate(Screen.Stats.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // Scan Screen
        composable(route = Screen.Scan.route) {
            ScanScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Manual Scan Screen
        composable(
            route = Screen.ManualScan.route,
            arguments = listOf(
                navArgument(NavArguments.BARCODE) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val barcode = backStackEntry.arguments?.getString(NavArguments.BARCODE) ?: ""
            ManualScanScreen(
                initialBarcode = barcode,
                paddingValues = paddingValues
            )
        }

        // History Screen
        composable(
            route = Screen.History.route,
            enterTransition = { fadeIn(animationSpec = tween(ANIMATION_DURATION)) },
            exitTransition = { fadeOut(animationSpec = tween(ANIMATION_DURATION)) },
            popEnterTransition = { fadeIn(animationSpec = tween(ANIMATION_DURATION)) },
            popExitTransition = { fadeOut(animationSpec = tween(ANIMATION_DURATION)) }
        ) {
            HistoryScreen(
                paddingValues = paddingValues,
                onNavigateToProduct = { barcode ->
                    navController.navigate(Screen.ProductDetail.createRoute(barcode))
                }
            )
        }

        // Product Detail Screen
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(
                navArgument(NavArguments.BARCODE) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val barcode = backStackEntry.arguments?.getString(NavArguments.BARCODE) ?: ""
            ProductDetailScreen(
                barcode = barcode,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Stats Screen
        composable(
            route = Screen.Stats.route,
            enterTransition = { fadeIn(animationSpec = tween(ANIMATION_DURATION)) },
            exitTransition = { fadeOut(animationSpec = tween(ANIMATION_DURATION)) },
            popEnterTransition = { fadeIn(animationSpec = tween(ANIMATION_DURATION)) },
            popExitTransition = { fadeOut(animationSpec = tween(ANIMATION_DURATION)) }
        ) {
            StatsScreen(
                paddingValues = paddingValues
            )
        }

        // Settings Screen
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                paddingValues = paddingValues
            )
        }
    }
}
