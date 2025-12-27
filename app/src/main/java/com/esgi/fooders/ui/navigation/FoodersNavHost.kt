package com.esgi.fooders.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.esgi.fooders.ui.editproduct.EditProductScreen
import com.esgi.fooders.ui.history.HistoryScreen
import com.esgi.fooders.ui.home.HomeScreen
import com.esgi.fooders.ui.login.LoginScreen
import com.esgi.fooders.ui.profile.ProfileScreen
import com.esgi.fooders.ui.scan.ManualScanScreen
import com.esgi.fooders.ui.scan.ScanScreen
import com.esgi.fooders.ui.settings.SettingsScreen

private const val ANIMATION_DURATION = 300

@Composable
fun FoodersNavHost(
    navController: NavHostController,
    startDestination: String,
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
        // Login Screen
        composable(
            route = Screen.Login.route,
            enterTransition = { fadeIn(animationSpec = tween(ANIMATION_DURATION)) },
            exitTransition = { fadeOut(animationSpec = tween(ANIMATION_DURATION)) }
        ) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Home Screen
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToScan = {
                    navController.navigate(Screen.Scan.route)
                },
                onNavigateToManualScan = {
                    navController.navigate(Screen.ManualScan.createRoute())
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        // Scan Screen
        composable(route = Screen.Scan.route) {
            ScanScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToManualScan = {
                    navController.navigate(Screen.ManualScan.createRoute())
                },
                onNavigateToEditProduct = { barcode, type ->
                    navController.navigate(Screen.EditProduct.createRoute(barcode, type))
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
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToEditProduct = { code, type ->
                    navController.navigate(Screen.EditProduct.createRoute(code, type))
                }
            )
        }

        // Edit Product Screen
        composable(
            route = Screen.EditProduct.route,
            arguments = listOf(
                navArgument(NavArguments.BARCODE) {
                    type = NavType.StringType
                },
                navArgument(NavArguments.TYPE) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val barcode = backStackEntry.arguments?.getString(NavArguments.BARCODE) ?: ""
            val type = backStackEntry.arguments?.getString(NavArguments.TYPE) ?: ""
            EditProductScreen(
                barcode = barcode,
                type = type,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onProductUpdated = {
                    // Navigate back to scan or manual scan
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                }
            )
        }

        // History Screen
        composable(route = Screen.History.route) {
            HistoryScreen(
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        // Profile Screen
        composable(route = Screen.Profile.route) {
            ProfileScreen(
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Settings Screen
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
