// presentation/src/main/kotlin/com/tinhtx/player/animation/MotionConstants.kt
package com.tinhtx.player.presentation.animation

object MotionConstants {
    const val ANIMATION_DURATION = 300
    const val SPRING_DAMPING = 0.8f
    const val SPRING_STIFFNESS = 200f

    // Animation durations
    const val DURATION_SHORT = 150
    const val DURATION_MEDIUM = 300
    const val DURATION_LONG = 500

    // Easing curves
    const val EASE_IN = "cubic-bezier(0.4, 0.0, 1, 1)"
    const val EASE_OUT = "cubic-bezier(0.0, 0.0, 0.2, 1)"
    const val EASE_IN_OUT = "cubic-bezier(0.4, 0.0, 0.2, 1)"

    // Spring configurations
    const val SPRING_DAMPING_RATIO_HIGH_BOUNCY = 0.2f
    const val SPRING_DAMPING_RATIO_MEDIUM_BOUNCY = 0.5f
    const val SPRING_DAMPING_RATIO_LOW_BOUNCY = 0.75f
    const val SPRING_DAMPING_RATIO_NO_BOUNCY = 1f

    const val SPRING_STIFFNESS_HIGH = 1400f
    const val SPRING_STIFFNESS_MEDIUM = 400f
    const val SPRING_STIFFNESS_LOW = 200f

    // Scale factors
    const val SCALE_FACTOR_SMALL = 0.9f
    const val SCALE_FACTOR_MEDIUM = 0.8f
    const val SCALE_FACTOR_LARGE = 0.7f

    // Alpha values
    const val ALPHA_TRANSPARENT = 0f
    const val ALPHA_SEMI_TRANSPARENT = 0.5f
    const val ALPHA_OPAQUE = 1f

    // Offset values
    const val OFFSET_SMALL = 16f
    const val OFFSET_MEDIUM = 32f
    const val OFFSET_LARGE = 64f
}
