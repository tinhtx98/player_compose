package com.tinhtx.player.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            // HomeScreen will be implemented in presentation module
        }

        composable(Screen.MusicPlayer.route) {
            // MusicPlayerScreen will be implemented in presentation module
        }

        composable(Screen.VideoPlayer.route) {
            // VideoPlayerScreen will be implemented in presentation module
        }

        composable(Screen.Collection.route) {
            // CollectionScreen will be implemented in presentation module
        }

        composable(Screen.Search.route) {
            // SearchScreen will be implemented in presentation module
        }

        composable(Screen.Settings.route) {
            // SettingsScreen will be implemented in presentation module
        }
    }
}
