// presentation/src/main/kotlin/com/tinhtx/player/presentation/navigation/AppNavigation.kt

package com.tinhtx.player.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tinhtx.player.core.navigation.Screen
import com.tinhtx.player.presentation.animation.animatedComposable
import com.tinhtx.player.presentation.component.common.MediaPermissionHandler
import com.tinhtx.player.presentation.screen.collection.CollectionScreen
import com.tinhtx.player.presentation.screen.main.HomeScreen
import com.tinhtx.player.presentation.screen.player.MusicPlayerScreen
import com.tinhtx.player.presentation.screen.player.VideoPlayerScreen
import com.tinhtx.player.presentation.screen.search.SearchScreen
import com.tinhtx.player.presentation.screen.settings.SettingsScreen

data class NavItem(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val label: String)

val navItems = listOf(
    NavItem(Screen.Home.route, Icons.Default.Home, "Home"),
    NavItem(Screen.Collection.route, Icons.Default.Collections, "Library"),
    NavItem(Screen.Search.route, Icons.Default.Search, "Search"),
    NavItem(Screen.Settings.route, Icons.Default.Settings, "Settings")
)

@Composable
fun AppNavigation() {
    var permissionsGranted by remember { mutableStateOf(false) }

    MediaPermissionHandler(
        onPermissionsGranted = {
            permissionsGranted = true
        }
    ) {
        AppNavigationContent(permissionsGranted = permissionsGranted)
    }
}

@Composable
private fun AppNavigationContent(permissionsGranted: Boolean) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route
    val showBottomBar = currentRoute != Screen.VideoPlayer.route && currentRoute != Screen.MusicPlayer.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    navItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.fillMaxSize()
            ) {
                animatedComposable(Screen.Home.route) {
                    HomeScreen(
                        permissionsGranted = permissionsGranted,
                        onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                        onNavigateToPlayer = { mediaId ->
                            // UPDATE: Sử dụng string template để navigate, fix argument mismatch (Screen.MusicPlayer là object, route là string const)
                            navController.navigate("${Screen.MusicPlayer.route.substringBefore("/{mediaId}")}/$mediaId")
                        },
                        onNavigateToVideoPlayer = { mediaId ->
                            navController.navigate("${Screen.VideoPlayer.route.substringBefore("/{mediaId}")}/$mediaId")
                        }
                    )
                }
                animatedComposable(Screen.Collection.route) {
                    CollectionScreen(onNavigateBack = { navController.popBackStack() }, onNavigateToPlayer = { mediaId ->
                        navController.navigate("${Screen.MusicPlayer.route.substringBefore("/{mediaId}")}/$mediaId")
                    })
                }
                animatedComposable(Screen.Search.route) {
                    SearchScreen(onNavigateBack = { navController.popBackStack() }, onNavigateToPlayer = { mediaId ->
                        navController.navigate("${Screen.MusicPlayer.route.substringBefore("/{mediaId}")}/$mediaId")
                    })
                }
                composable(
                    route = Screen.MusicPlayer.route,
                    arguments = Screen.MusicPlayer.arguments,
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() }
                ) { backStackEntry ->
                    val mediaId = backStackEntry.arguments?.getString("mediaId") ?: ""
                    MusicPlayerScreen(
                        mediaId = mediaId,
                        onNavigateBack = { navController.popBackStack() })
                }
                composable(
                    route = Screen.VideoPlayer.route,
                    arguments = Screen.VideoPlayer.arguments,
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() }
                ) { backStackEntry ->
                    val mediaId = backStackEntry.arguments?.getString("mediaId") ?: ""
                    VideoPlayerScreen(
                        mediaId = mediaId, onNavigateBack = { navController.popBackStack() })
                }
                animatedComposable(Screen.Settings.route) {
                    SettingsScreen(onNavigateBack = { navController.popBackStack() })
                }
            }
        }
    }
}
