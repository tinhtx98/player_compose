// core/src/main/kotlin/com/tinhtx/player/util/SystemUIController.kt
package com.tinhtx.player.core.util

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun SystemBarsController(
    statusBarColor: Color,
    navigationBarColor: Color,
    isAppearanceLightStatusBars: Boolean = true,  // True for dark icons on light background
    isAppearanceLightNavigationBars: Boolean = true,
    isStatusBarVisible: Boolean = true,
    isNavigationBarVisible: Boolean = true
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as? Activity ?: return@SideEffect
            val window = activity.window
            window.statusBarColor = statusBarColor.toArgb()
            window.navigationBarColor = navigationBarColor.toArgb()

            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = isAppearanceLightStatusBars
            insetsController.isAppearanceLightNavigationBars = isAppearanceLightNavigationBars

            // Kiểm soát visibility
            insetsController.systemBarsBehavior = if (isStatusBarVisible) {
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
            } else {
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            if (isStatusBarVisible) insetsController.show(WindowInsetsCompat.Type.statusBars()) else insetsController.hide(
                WindowInsetsCompat.Type.statusBars()
            )
            if (isNavigationBarVisible) insetsController.show(WindowInsetsCompat.Type.navigationBars()) else insetsController.hide(
                WindowInsetsCompat.Type.navigationBars()
            )
        }
    }
}
