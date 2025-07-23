// core/src/main/kotlin/com/tinhtx/player/util/SystemUIController.kt
package com.tinhtx.player.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun rememberSystemUiControllerWrapper(): SystemUiControllerWrapper {
    val controller = rememberSystemUiController()
    return remember { SystemUiControllerWrapper(controller) }
}

class SystemUiControllerWrapper(private val controller: SystemUiController) {
    fun setStatusBarColor(color: Color, darkIcons: Boolean) {
        controller.setStatusBarColor(color, darkIcons)
    }

    fun setNavigationBarColor(color: Color, darkIcons: Boolean) {
        controller.setNavigationBarColor(color, darkIcons)
    }

    fun setSystemBarsColor(color: Color, darkIcons: Boolean) {
        controller.setSystemBarsColor(color, darkIcons)
    }

    var isStatusBarVisible: Boolean
        get() = controller.isStatusBarVisible
        set(value) {
            controller.isStatusBarVisible = value
        }

    var isNavigationBarVisible: Boolean
        get() = controller.isNavigationBarVisible
        set(value) {
            controller.isNavigationBarVisible = value
        }
}
