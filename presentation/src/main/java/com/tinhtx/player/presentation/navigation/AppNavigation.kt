// presentation/src/main/kotlin/com/tinhtx/player/navigation/AppNavigation.kt  // UPDATE: Giữ ở :presentation để tránh phụ thuộc vòng, như giải pháp trước. Nếu ban đầu ở :core, bạn có thể di chuyển lại, nhưng sẽ cần fix phụ thuộc.

package com.tinhtx.player.presentation.navigation  // UPDATE: Package ở presentation để chứa navigation UI-related

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tinhtx.player.core.navigation.Screen  // UPDATE: Import Screen từ :core (phụ thuộc an toàn, :presentation phụ thuộc :core, không ngược lại)
import com.tinhtx.player.presentation.screen.collection.CollectionScreen  // UPDATE: Import screens từ cùng module :presentation
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
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }
        ) {
            HomeScreen(
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToPlayer = { mediaId ->
                    // UPDATE: Sử dụng string template để navigate, fix argument mismatch (Screen.MusicPlayer là object, route là string const)
                    navController.navigate("${Screen.MusicPlayer.route.substringBefore("/{mediaId}")}/$mediaId")
                }
            )
        }

        composable(
            route = Screen.Collection.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
        ) {
            CollectionScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlayer = { mediaId ->
                    navController.navigate("${Screen.MusicPlayer.route.substringBefore("/{mediaId}")}/$mediaId")
                }
            )
        }

        composable(
            route = Screen.Search.route,
            enterTransition = { slideInVertically(initialOffsetY = { -it }) + fadeIn() },
            exitTransition = { slideOutVertically(targetOffsetY = { -it }) + fadeOut() }
        ) {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlayer = { mediaId ->
                    navController.navigate("${Screen.MusicPlayer.route.substringBefore("/{mediaId}")}/$mediaId")
                }
            )
        }

        composable(
            route = Screen.MusicPlayer.route,
            enterTransition = { slideInVertically(initialOffsetY = { it }) + fadeIn() },
            exitTransition = { slideOutVertically(targetOffsetY = { it }) + fadeOut() }
        ) { backStackEntry ->
            val mediaId = backStackEntry.arguments?.getString("mediaId") ?: ""
            MusicPlayerScreen(
                mediaId = mediaId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToVideoPlayer = { videoId ->
                    navController.navigate("${Screen.VideoPlayer.route.substringBefore("/{mediaId}")}/$videoId")
                }
            )
        }

        composable(
            route = Screen.VideoPlayer.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { backStackEntry ->
            val mediaId = backStackEntry.arguments?.getString("mediaId") ?: ""
            VideoPlayerScreen(
                mediaId = mediaId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Settings.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
        ) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
