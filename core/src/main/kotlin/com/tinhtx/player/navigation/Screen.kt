package com.tinhtx.player.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object MusicPlayer : Screen("music_player")
    object VideoPlayer : Screen("video_player")
    object Collection : Screen("collection")
    object Search : Screen("search")
    object Settings : Screen("settings")

    // Screen with arguments
    object MusicPlayerWithId : Screen("music_player/{mediaId}") {
        fun createRoute(mediaId: String) = "music_player/$mediaId"
    }

    object VideoPlayerWithId : Screen("video_player/{mediaId}") {
        fun createRoute(mediaId: String) = "video_player/$mediaId"
    }
}
