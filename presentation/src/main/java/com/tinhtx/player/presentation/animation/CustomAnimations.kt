// presentation/src/main/kotlin/com/tinhtx/player/animation/CustomAnimations.kt
package com.tinhtx.player.presentation.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.IntOffset
import com.tinhtx.player.domain.model.AgeGroup

@Composable
fun FlowerBloomAnimation(
    visible: Boolean, ageGroup: AgeGroup, content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        content()
    }
}

@Composable
fun WaterDropAnimation(
    visible: Boolean, ageGroup: AgeGroup, content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        content()
    }
}

@Composable
fun FallingLeavesAnimation(
    visible: Boolean, ageGroup: AgeGroup, content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible, enter = fadeIn(), exit = fadeOut()
    ) {
        content()
    }
}

@Composable
fun WaterWaveAnimation(
    visible: Boolean, ageGroup: AgeGroup, content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible, enter = fadeIn(), exit = fadeOut()
    ) {
        content()
    }
}

// Fade animations
@Composable
fun fadeIn(duration: Int = MotionConstants.DURATION_MEDIUM): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = duration,
            easing = FastOutSlowInEasing
        ),
        initialAlpha = MotionConstants.ALPHA_TRANSPARENT
    )
}

@Composable
fun fadeOut(duration: Int = MotionConstants.DURATION_MEDIUM): ExitTransition {
    return fadeOut(
        animationSpec = tween(
            durationMillis = duration,
            easing = FastOutSlowInEasing
        ),
        targetAlpha = MotionConstants.ALPHA_TRANSPARENT
    )
}

// Scale animations
@Composable
fun scaleIn(
    duration: Int = MotionConstants.DURATION_MEDIUM,
    initialScale: Float = MotionConstants.SCALE_FACTOR_SMALL
): EnterTransition {
    return scaleIn(
        animationSpec = tween(
            durationMillis = duration,
            easing = FastOutSlowInEasing
        ),
        initialScale = initialScale,
        transformOrigin = TransformOrigin.Center
    )
}

@Composable
fun scaleOut(
    duration: Int = MotionConstants.DURATION_MEDIUM,
    targetScale: Float = MotionConstants.SCALE_FACTOR_SMALL
): ExitTransition {
    return scaleOut(
        animationSpec = tween(
            durationMillis = duration,
            easing = FastOutSlowInEasing
        ),
        targetScale = targetScale,
        transformOrigin = TransformOrigin.Center
    )
}

// Slide animations
@Composable
fun slideInFromBottom(duration: Int = MotionConstants.DURATION_MEDIUM): EnterTransition {
    return slideInVertically(
        animationSpec = tween(
            durationMillis = duration,
            easing = FastOutSlowInEasing
        ),
        initialOffsetY = { it }
    )
}

@Composable
fun slideOutToBottom(duration: Int = MotionConstants.DURATION_MEDIUM): ExitTransition {
    return slideOutVertically(
        animationSpec = tween(
            durationMillis = duration,
            easing = FastOutSlowInEasing
        ),
        targetOffsetY = { it }
    )
}

@Composable
fun slideInFromRight(duration: Int = MotionConstants.DURATION_MEDIUM): EnterTransition {
    return slideInHorizontally(
        animationSpec = tween(
            durationMillis = duration,
            easing = FastOutSlowInEasing
        ),
        initialOffsetX = { it }
    )
}

@Composable
fun slideOutToLeft(duration: Int = MotionConstants.DURATION_MEDIUM): ExitTransition {
    return slideOutHorizontally(
        animationSpec = tween(
            durationMillis = duration,
            easing = FastOutSlowInEasing
        ),
        targetOffsetX = { -it }
    )
}

// Combined animations
@Composable
fun fadeInWithSlide(
    duration: Int = MotionConstants.DURATION_MEDIUM,
    offset: Float = MotionConstants.OFFSET_MEDIUM
): EnterTransition {
    return fadeIn(duration) + slideInVertically(
        animationSpec = tween(
            durationMillis = duration,
            easing = FastOutSlowInEasing
        ),
        initialOffsetY = { (it * offset / 100).toInt() }
    )
}

@Composable
fun fadeOutWithSlide(
    duration: Int = MotionConstants.DURATION_MEDIUM,
    offset: Float = MotionConstants.OFFSET_MEDIUM
): ExitTransition {
    return fadeOut(duration) + slideOutVertically(
        animationSpec = tween(
            durationMillis = duration,
            easing = FastOutSlowInEasing
        ),
        targetOffsetY = { -(it * offset / 100).toInt() }
    )
}

// Spring animations
fun springSpec(
    dampingRatio: Float = MotionConstants.SPRING_DAMPING_RATIO_LOW_BOUNCY,
    stiffness: Float = MotionConstants.SPRING_STIFFNESS_MEDIUM
): SpringSpec<Float> {
    return spring(
        dampingRatio = dampingRatio,
        stiffness = stiffness
    )
}

fun springSpecInt(
    dampingRatio: Float = MotionConstants.SPRING_DAMPING_RATIO_LOW_BOUNCY,
    stiffness: Float = MotionConstants.SPRING_STIFFNESS_MEDIUM
): SpringSpec<IntOffset> {
    return spring(
        dampingRatio = dampingRatio,
        stiffness = stiffness
    )
}
