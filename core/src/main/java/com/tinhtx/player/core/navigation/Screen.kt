// core/src/main/kotlin/com/tinhtx/player/navigation/Screen.kt  // UPDATE: Giữ ở :core (như ban đầu), vì Screen là model dùng chung, không phụ thuộc UI. Nếu đã di chuyển sang :presentation, hãy giữ nguyên để tránh thay đổi lớn.

package com.tinhtx.player.core.navigation  // UPDATE: Giữ package như ban đầu để map với code cũ

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

    object MusicPlayer : Screen(  // UPDATE: Thay data class thành object để dễ truy cập route trực tiếp (fix unresolved reference 'route')
        route = "music_player/{mediaId}",
        arguments = listOf(
            navArgument("mediaId") { type = NavType.StringType }
        )
    )

    object VideoPlayer : Screen(  // UPDATE: Tương tự, thay data class thành object
        route = "video_player/{mediaId}",
        arguments = listOf(
            navArgument("mediaId") { type = NavType.StringType }
        )
    )
}
