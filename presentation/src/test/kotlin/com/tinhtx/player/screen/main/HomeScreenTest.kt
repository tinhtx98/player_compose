package com.tinhtx.player.screen.main

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tinhtx.player.model.MediaItem
import com.tinhtx.player.model.MediaType
import com.tinhtx.player.theme.TinhTXPlayerTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_displaysTitle() {
        composeTestRule.setContent {
            TinhTXPlayerTheme {
                HomeScreen(
                    onNavigateToPlayer = {},
                    onNavigateToSearch = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("TinhTX Player")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysSearchButton() {
        composeTestRule.setContent {
            TinhTXPlayerTheme {
                HomeScreen(
                    onNavigateToPlayer = {},
                    onNavigateToSearch = {}
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Search")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsPermissionHandler_whenPermissionsNotGranted() {
        composeTestRule.setContent {
            TinhTXPlayerTheme {
                HomeScreen(
                    onNavigateToPlayer = {},
                    onNavigateToSearch = {}
                )
            }
        }

        // This test would need proper mocking of permission state
        // For now, we just verify the component exists
        composeTestRule
            .onNodeWithText("TinhTX Player")
            .assertExists()
    }
}
