// presentation/src/main/kotlin/com/tinhtx/player/navigation/AppNavigation.kt  // UPDATE: Giữ ở :presentation để tránh phụ thuộc vòng, như giải pháp trước. Nếu ban đầu ở :core, bạn có thể di chuyển lại, nhưng sẽ cần fix phụ thuộc.

package com.tinhtx.player.presentation.navigation  // UPDATE: Package ở presentation để chứa navigation UI-related

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tinhtx.player.core.navigation.Screen
import com.tinhtx.player.presentation.component.common.MediaPermissionHandler
import com.tinhtx.player.presentation.screen.collection.CollectionScreen
import com.tinhtx.player.presentation.screen.main.HomeScreen
import com.tinhtx.player.presentation.screen.player.MusicPlayerScreen
import com.tinhtx.player.presentation.screen.player.VideoPlayerScreen
import com.tinhtx.player.presentation.screen.search.SearchScreen
import com.tinhtx.player.presentation.screen.settings.SettingsScreen

data class NavItem(val route: String, val icon: @Composable () -> Unit, val label: String)

val navItems = listOf(
    NavItem(Screen.Home.route, { Icons.Default.Home }, "Home"),
    NavItem(Screen.Collection.route, { Icons.Default.Collections }, "Library"),
    NavItem(Screen.Search.route, { Icons.Default.Search }, "Player"),
    NavItem(Screen.Settings.route, { Icons.Default.Settings }, "Settings")
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
    val currentRoute = navBackStackEntry?.destination?.route

    // Ẩn nav bar khi ở VideoPlayerScreen
    val showBottomBar = currentRoute != Screen.VideoPlayer.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                // Thêm Bottom Navigation Bar
                NavigationBar {
                    val currentDestination = navBackStackEntry?.destination

                    navItems.forEach { item ->
                        NavigationBarItem(
                            icon = item.icon,
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    // Logic để tránh stack chồng (single top)
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            })
                    }
                }
            }
        }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = if (showBottomBar) Modifier.padding(innerPadding) else Modifier.fillMaxSize()
        ) {
            composable(
                route = Screen.Home.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
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

            composable(
                route = Screen.Collection.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }) {
                CollectionScreen(onNavigateBack = { navController.popBackStack() }, onNavigateToPlayer = { mediaId ->
                    navController.navigate("${Screen.MusicPlayer.route.substringBefore("/{mediaId}")}/$mediaId")
                })
            }

            composable(
                route = Screen.Search.route,
                enterTransition = { slideInVertically(initialOffsetY = { -it }) + fadeIn() },
                exitTransition = { slideOutVertically(targetOffsetY = { -it }) + fadeOut() }) {
                SearchScreen(onNavigateBack = { navController.popBackStack() }, onNavigateToPlayer = { mediaId ->
                    navController.navigate("${Screen.MusicPlayer.route.substringBefore("/{mediaId}")}/$mediaId")
                })
            }

            composable(
                route = Screen.MusicPlayer.route,
                enterTransition = { slideInVertically(initialOffsetY = { it }) + fadeIn() },
                exitTransition = { slideOutVertically(targetOffsetY = { it }) + fadeOut() }) { backStackEntry ->
                val mediaId = backStackEntry.arguments?.getString("mediaId") ?: ""
                MusicPlayerScreen(
                    mediaId = mediaId,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToVideoPlayer = { videoId ->
                        navController.navigate("${Screen.VideoPlayer.route.substringBefore("/{mediaId}")}/$videoId")
                    })
            }

            composable(
                route = Screen.VideoPlayer.route,
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() }) { backStackEntry ->
                val mediaId = backStackEntry.arguments?.getString("mediaId") ?: ""
                VideoPlayerScreen(
                    mediaId = mediaId, onNavigateBack = { navController.popBackStack() })
            }

            composable(
                route = Screen.Settings.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }) {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}
