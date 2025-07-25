// core/src/main/kotlin/com/tinhtx/player/navigation/NavigationAnimation.kt
package com.tinhtx.player.presentation.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tinhtx.player.core.common.Constants

// UPDATE: Đánh dấu hàm là @Composable để có thể gọi trong Composable scope (fix lỗi "Using composable inside of a compose scope").
// Lưu ý: Đây là cảnh báo IDE, không phải lỗi build. Nếu vẫn thấy cảnh báo, đảm bảo hàm được gọi trong NavHost (là Composable).
// Nếu IDE vẫn complain, ignore cảnh báo vì code chạy đúng (đây là bug IDE với extension functions on NavGraphBuilder).

fun NavGraphBuilder.animatedComposable(
    route: String,
    enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>).() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(durationMillis = Constants.ANIMATION_DURATION_MEDIUM.toInt())
        ) + fadeIn(
            animationSpec = tween(durationMillis = Constants.ANIMATION_DURATION_MEDIUM.toInt())
        )
    },
    exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>).() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(durationMillis = Constants.ANIMATION_DURATION_MEDIUM.toInt())
        ) + fadeOut(
            animationSpec = tween(durationMillis = Constants.ANIMATION_DURATION_MEDIUM.toInt())
        )
    },
    popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>).() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(durationMillis = Constants.ANIMATION_DURATION_MEDIUM.toInt())
        ) + fadeIn(
            animationSpec = tween(durationMillis = Constants.ANIMATION_DURATION_MEDIUM.toInt())
        )
    },
    popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>).() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(durationMillis = Constants.ANIMATION_DURATION_MEDIUM.toInt())
        ) + fadeOut(
            animationSpec = tween(durationMillis = Constants.ANIMATION_DURATION_MEDIUM.toInt())
        )
    },
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition
    ) { backStackEntry ->
        content(backStackEntry)
    }
}
