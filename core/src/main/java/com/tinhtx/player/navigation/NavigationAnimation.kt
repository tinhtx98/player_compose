// core/src/main/kotlin/com/tinhtx/player/navigation/NavigationAnimation.kt
package com.tinhtx.player.core.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tinhtx.player.common.Constants

@Composable
fun NavGraphBuilder.animatedComposable(
    route: String,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(durationMillis = Constants.ANIMATION_DURATION_MEDIUM.toInt())
            ) + fadeIn(
                animationSpec = tween(durationMillis = Constants.ANIMATION_DURATION_MEDIUM.toInt())
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(durationMillis = Constants.ANIMATION_DURATION_MEDIUM.toInt())
            ) + fadeOut(
                animationSpec = tween(durationMillis = Constants.ANIMATION_DURATION_MEDIUM.toInt())
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(durationMillis = Constants.ANIMATION_DURATION_MEDIUM.toInt())
            ) + fadeIn(
                animationSpec = tween(durationMillis = Constants.ANIMATION_DURATION_MEDIUM.toInt())
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(durationMillis = Constants.ANIMATION_DURATION_MEDIUM.toInt())
            ) + fadeOut(
                animationSpec = tween(durationMillis = Constants.ANIMATION_DURATION_MEDIUM.toInt())
            )
        }
    ) {
        content(it)
    }
}
