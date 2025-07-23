// core/src/main/kotlin/com/tinhtx/player/navigation/Screen.kt
package com.tinhtx.player.core.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : Screen("home")
    data object Collection : Screen("collection")
    data object Search : Screen("search")
    data object Settings : Screen("settings")

    data class MusicPlayer(
        val mediaId: String = "{mediaId}"
    ) : Screen(
        route = "music_player/{mediaId}",
        arguments = listOf(
            navArgument("mediaId") { type = NavType.StringType }
        )
    )

    data class VideoPlayer(
        val mediaId: String = "{mediaId}"
    ) : Screen(
        route = "video_player/{mediaId}",
        arguments = listOf(
            navArgument("mediaId") { type = NavType.StringType }
        )
    )
}
