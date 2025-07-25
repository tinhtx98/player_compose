// presentation/src/main/kotlin/com/tinhtx/player/screen/settings/SettingsScreen.kt
package com.tinhtx.player.presentation.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tinhtx.player.core.common.formatAsFileSize
import com.tinhtx.player.domain.model.AgeGroup
import com.tinhtx.player.domain.model.PlaybackSettings
import com.tinhtx.player.domain.model.PrivacySettings
import com.tinhtx.player.domain.model.ThemeMode
import com.tinhtx.player.presentation.animation.FlowerBloomAnimation
import com.tinhtx.player.presentation.animation.WaterDropAnimation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val userPreferences by viewModel.userPreferences.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Bar as composable - không dùng Scaffold
        /*TopAppBar(
            title = { Text("Cài Đặt") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )*/
        // Main content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Giao Diện Section
            item {
                WaterDropAnimation(
                    visible = true,
                    ageGroup = userPreferences.ageGroup
                ) {
                    SettingsSection(
                        title = "Giao Diện",
                        icon = Icons.Default.Palette
                    ) {
                        AgeGroupSelector(
                            selectedAgeGroup = userPreferences.ageGroup,
                            onAgeGroupChange = { viewModel.updateAgeGroup(it) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        ThemeSelector(
                            selectedTheme = userPreferences.themeMode,
                            onThemeChange = { viewModel.updateThemeMode(it) }
                        )
                    }
                }
            }

            // Âm Thanh Section
            item {
                FlowerBloomAnimation(
                    visible = true,
                    ageGroup = userPreferences.ageGroup
                ) {
                    SettingsSection(
                        title = "Âm Thanh",
                        icon = Icons.Default.VolumeUp
                    ) {
                        AudioSettingsContent(
                            playbackSettings = userPreferences.playbackSettings,
                            onCrossfadeDurationChange = { viewModel.updateCrossfadeDuration(it) },
                            onGaplessPlaybackChange = { viewModel.updateGaplessPlayback(it) },
                            onAutoPlayNextChange = { viewModel.updateAutoPlayNext(it) }
                        )
                    }
                }
            }

            // Quyền Riêng Tư Section
            item {
                WaterDropAnimation(
                    visible = true,
                    ageGroup = userPreferences.ageGroup
                ) {
                    SettingsSection(
                        title = "Quyền Riêng Tư",
                        icon = Icons.Default.Security
                    ) {
                        PrivacySettingsContent(
                            privacySettings = userPreferences.privacySettings,
                            onAllowAnalyticsChange = { viewModel.updateAllowAnalytics(it) },
                            onAllowCrashReportingChange = { viewModel.updateAllowCrashReporting(it) }
                        )
                    }
                }
            }

            // Bộ Nhớ Section
            item {
                FlowerBloomAnimation(
                    visible = true,
                    ageGroup = userPreferences.ageGroup
                ) {
                    SettingsSection(
                        title = "Bộ Nhớ",
                        icon = Icons.Default.Storage
                    ) {
                        StorageSettingsContent(
                            cacheSize = uiState.cacheSize,
                            onClearCache = { viewModel.clearCache() },
                            onClearAllData = { viewModel.clearAllData() }
                        )
                    }
                }
            }

            // Thông Tin Ứng Dụng Section
            item {
                WaterDropAnimation(
                    visible = true,
                    ageGroup = userPreferences.ageGroup
                ) {
                    SettingsSection(
                        title = "Thông Tin",
                        icon = Icons.Default.Person
                    ) {
                        AppInfoContent(
                            appVersion = uiState.appVersion,
                            buildNumber = uiState.buildNumber
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AgeGroupSelector(
    selectedAgeGroup: AgeGroup,
    onAgeGroupChange: (AgeGroup) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Độ Tuổi Giao Diện",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Chọn độ tuổi để tối ưu giao diện phù hợp",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedAgeGroup.displayName,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(selectedAgeGroup.cornerRadius.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                AgeGroup.values().forEach { ageGroup ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = ageGroup.displayName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = ageGroup.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        },
                        onClick = {
                            onAgeGroupChange(ageGroup)
                            expanded = false
                        },
                        leadingIcon = {
                            RadioButton(
                                selected = selectedAgeGroup == ageGroup,
                                onClick = {
                                    onAgeGroupChange(ageGroup)
                                    expanded = false
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeSelector(
    selectedTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit
) {
    Column {
        Text(
            text = "Giao Diện Sáng/Tối",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        ThemeMode.values().forEach { theme ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedTheme == theme,
                        onClick = { onThemeChange(theme) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedTheme == theme,
                    onClick = null
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = when (theme) {
                        ThemeMode.LIGHT -> "Sáng"
                        ThemeMode.DARK -> "Tối"
                        ThemeMode.SYSTEM -> "Theo hệ thống"
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun AudioSettingsContent(
    playbackSettings: PlaybackSettings,
    onCrossfadeDurationChange: (Long) -> Unit,
    onGaplessPlaybackChange: (Boolean) -> Unit,
    onAutoPlayNextChange: (Boolean) -> Unit
) {
    Column {
        // Crossfade Duration
        Text(
            text = "Thời gian chuyển cảnh: ${playbackSettings.crossfadeDuration / 1000}s",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Slider(
            value = playbackSettings.crossfadeDuration.toFloat(),
            onValueChange = { onCrossfadeDurationChange(it.toLong()) },
            valueRange = 0f..10000f,
            steps = 10,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Gapless Playback
        ListItem(
            headlineContent = { Text("Phát liên tục") },
            supportingContent = { Text("Không có khoảng lặng giữa các bài") },
            trailingContent = {
                Switch(
                    checked = playbackSettings.gaplessPlayback,
                    onCheckedChange = onGaplessPlaybackChange
                )
            }
        )

        // Auto Play Next
        ListItem(
            headlineContent = { Text("Tự động phát tiếp") },
            supportingContent = { Text("Tự động phát bài kế tiếp khi kết thúc") },
            trailingContent = {
                Switch(
                    checked = playbackSettings.autoPlayNext,
                    onCheckedChange = onAutoPlayNextChange
                )
            }
        )
    }
}

@Composable
private fun PrivacySettingsContent(
    privacySettings: PrivacySettings,
    onAllowAnalyticsChange: (Boolean) -> Unit,
    onAllowCrashReportingChange: (Boolean) -> Unit
) {
    Column {
        ListItem(
            headlineContent = { Text("Phân tích sử dụng") },
            supportingContent = { Text("Giúp cải thiện ứng dụng") },
            trailingContent = {
                Switch(
                    checked = privacySettings.allowAnalytics,
                    onCheckedChange = onAllowAnalyticsChange
                )
            }
        )

        ListItem(
            headlineContent = { Text("Báo cáo lỗi") },
            supportingContent = { Text("Tự động gửi báo cáo khi có lỗi") },
            trailingContent = {
                Switch(
                    checked = privacySettings.allowCrashReporting,
                    onCheckedChange = onAllowCrashReportingChange
                )
            }
        )
    }
}

@Composable
private fun StorageSettingsContent(
    cacheSize: Long,
    onClearCache: () -> Unit,
    onClearAllData: () -> Unit
) {
    Column {
        ListItem(
            headlineContent = { Text("Bộ nhớ đệm") },
            supportingContent = { Text(cacheSize.formatAsFileSize()) },
            trailingContent = {
                Button(onClick = onClearCache) {
                    Text("Xóa")
                }
            }
        )

        ListItem(
            headlineContent = { Text("Xóa tất cả dữ liệu") },
            supportingContent = { Text("Xóa toàn bộ dữ liệu ứng dụng") },
            trailingContent = {
                Button(
                    onClick = onClearAllData,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Xóa tất cả")
                }
            }
        )
    }
}

@Composable
private fun AppInfoContent(
    appVersion: String,
    buildNumber: String
) {
    Column {
        ListItem(
            headlineContent = { Text("Phiên bản") },
            supportingContent = { Text(appVersion) }
        )

        ListItem(
            headlineContent = { Text("Build số") },
            supportingContent = { Text(buildNumber) }
        )

        ListItem(
            headlineContent = { Text("Nhà phát triển") },
            supportingContent = { Text("TinhTX Development Team") }
        )
    }
}

// Extension properties for AgeGroup
val AgeGroup.displayName: String
    get() = when (this) {
        AgeGroup.CHILD -> "Trẻ Em (0-12 tuổi)"
        AgeGroup.TEEN -> "Thanh Thiếu Niên (13-17 tuổi)"
        AgeGroup.ADULT -> "Người Lớn (18+ tuổi)"
    }

val AgeGroup.description: String
    get() = when (this) {
        AgeGroup.CHILD -> "Giao diện tròn, màu sắc tươi sáng, chữ to dễ đọc"
        AgeGroup.TEEN -> "Phong cách hiện đại, animation mượt mà, màu sắc sống động"
        AgeGroup.ADULT -> "Thiết kế tối giản, chuyên nghiệp, dễ sử dụng"
    }
