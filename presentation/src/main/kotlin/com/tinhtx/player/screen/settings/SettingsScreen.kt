package com.tinhtx.player.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tinhtx.player.component.media.EqualizerComponents
import com.tinhtx.player.model.AppTheme
import com.tinhtx.player.model.UserSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val userSettings by viewModel.userSettings.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Theme Settings
                SettingsCard(
                    title = "Appearance",
                    content = {
                        ThemeSelector(
                            currentTheme = userSettings.theme,
                            onThemeSelected = { viewModel.updateTheme(it) }
                        )
                    }
                )
            }

            item {
                // Playback Settings
                SettingsCard(
                    title = "Playback",
                    content = {
                        PlaybackSettings(
                            settings = userSettings,
                            onSettingsChange = { viewModel.updateSettings(it) }
                        )
                    }
                )
            }

            item {
                // Audio Effects
                EqualizerComponents(
                    bassBoostLevel = userSettings.bassBoostLevel,
                    virtualizerLevel = userSettings.virtualizerLevel,
                    onBassBoostChange = { viewModel.updateBassBoost(it) },
                    onVirtualizerChange = { viewModel.updateVirtualizer(it) }
                )
            }
        }
    }
}

@Composable
private fun SettingsCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}

@Composable
private fun ThemeSelector(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit
) {
    Column {
        AppTheme.values().forEach { theme ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = currentTheme == theme,
                    onClick = { onThemeSelected(theme) }
                )
                Text(
                    text = when (theme) {
                        AppTheme.LIGHT -> "Light"
                        AppTheme.DARK -> "Dark"
                        AppTheme.SYSTEM -> "System"
                    },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun PlaybackSettings(
    settings: UserSettings,
    onSettingsChange: (UserSettings) -> Unit
) {
    Column {
        // Auto Play Next
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Auto play next")
            Switch(
                checked = settings.autoPlayNext,
                onCheckedChange = {
                    onSettingsChange(settings.copy(autoPlayNext = it))
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Show Notifications
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show notifications")
            Switch(
                checked = settings.showNotification,
                onCheckedChange = {
                    onSettingsChange(settings.copy(showNotification = it))
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Skip Silence
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Skip silence")
            Switch(
                checked = settings.skipSilence,
                onCheckedChange = {
                    onSettingsChange(settings.copy(skipSilence = it))
                }
            )
        }
    }
}
