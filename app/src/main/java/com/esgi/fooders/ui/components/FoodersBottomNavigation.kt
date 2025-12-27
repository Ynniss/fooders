package com.esgi.fooders.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.esgi.fooders.ui.navigation.Screen

data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = Screen.Home.route,
        label = "Accueil",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavItem(
        route = Screen.History.route,
        label = "Historique",
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History
    ),
    BottomNavItem(
        route = Screen.Profile.route,
        label = "Profil",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
)

@Composable
fun FoodersBottomNavigation(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Only show bottom nav on main screens
    val showBottomNav = currentDestination?.route in bottomNavItems.map { it.route }

    if (showBottomNav) {
        NavigationBar(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            bottomNavItems.forEach { item ->
                val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(item.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label
                        )
                    },
                    label = {
                        Text(text = item.label)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }
    }
}

@Composable
fun shouldShowBottomBar(navController: NavController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    return currentRoute in bottomNavItems.map { it.route }
}
