// app/src/main/java/com/tinhtx/player/MainActivity.kt
package com.tinhtx.player

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.tinhtx.player.core.util.SystemBarsController
import com.tinhtx.player.presentation.navigation.AppNavigation
import com.tinhtx.player.presentation.theme.TinhTXTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TinhTXTheme {
                SystemBarsController(  // Sử dụng ở đây hoặc trong screens
                    statusBarColor = Color.Transparent,
                    navigationBarColor = Color.Transparent,
                )
                AppNavigation()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TinhTXTheme {
        AppNavigation()
    }
}
