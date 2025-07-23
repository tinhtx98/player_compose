// core/src/main/kotlin/com/tinhtx/player/navigation/AppNavigation.kt
package com.tinhtx.player.core.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tinhtx.player.presentation.screen.collection.CollectionScreen
import com.tinhtx.player.presentation.screen.main.HomeScreen
import com.tinhtx.player.presentation.screen.player.MusicPlayerScreen
import com.tinhtx.player.presentation.screen.player.VideoPlayerScreen
import com.tinhtx.player.presentation.screen.search.SearchScreen
import com.tinhtx.player.presentation.screen.settings.SettingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home>(
            enterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
            }
        ) {
            HomeScreen(
                onNavigateToSearch = {
                    navController.navigate(Screen.Search)
                },
                onNavigateToPlayer = { mediaId ->
                    navController.navigate(
                        Screen.MusicPlayer(mediaId = mediaId)
                    )
                }
            )
        }

        composable<Screen.Collection>(
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }) + fadeIn()
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
            }
        ) {
            CollectionScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToPlayer = { mediaId ->
                    navController.navigate(
                        Screen.MusicPlayer(mediaId = mediaId)
                    )
                }
            )
        }

        composable<Screen.Search>(
            enterTransition = {
                slideInVertically(initialOffsetY = { -it }) + fadeIn()
            },
            exitTransition = {
                slideOutVertically(targetOffsetY = { -it }) + fadeOut()
            }
        ) {
            SearchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToPlayer = { mediaId ->
                    navController.navigate(
                        Screen.MusicPlayer(mediaId = mediaId)
                    )
                }
            )
        }

        composable<Screen.MusicPlayer>(
            enterTransition = {
                slideInVertically(initialOffsetY = { it }) + fadeIn()
            },
            exitTransition = {
                slideOutVertically(targetOffsetY = { it }) + fadeOut()
            }
        ) { backStackEntry ->
            val args = backStackEntry.arguments
            val mediaId = args?.getString("mediaId") ?: ""

            MusicPlayerScreen(
                mediaId = mediaId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToVideoPlayer = { videoId ->
                    navController.navigate(
                        Screen.VideoPlayer(mediaId = videoId)
                    )
                }
            )
        }

        composable<Screen.VideoPlayer>(
            enterTransition = {
                fadeIn()
            },
            exitTransition = {
                fadeOut()
            }
        ) { backStackEntry ->
            val args = backStackEntry.arguments
            val mediaId = args?.getString("mediaId") ?: ""

            VideoPlayerScreen(
                mediaId = mediaId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<Screen.Settings>(
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }) + fadeIn()
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
            }
        ) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
