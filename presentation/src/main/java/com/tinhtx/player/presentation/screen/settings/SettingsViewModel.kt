// presentation/src/main/kotlin/com/tinhtx/player/screen/settings/SettingsViewModel.kt
package com.tinhtx.player.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinhtx.player.data.local.preferences.UserPreferencesManager
import com.tinhtx.player.domain.model.AgeGroup
import com.tinhtx.player.domain.model.AnimationIntensity
import com.tinhtx.player.domain.model.SortBy
import com.tinhtx.player.domain.model.SortOrder
import com.tinhtx.player.domain.model.ThemeMode
import com.tinhtx.player.domain.model.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    val userPreferences: StateFlow<UserPreferences> = userPreferencesManager.getUserPreferences()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences(
                ageGroup = AgeGroup.ADULT,
                themeMode = ThemeMode.SYSTEM,
                primaryColor = null,
                language = "vi",
                playbackSettings = PlaybackSettings(),
                equalizerSettings = EqualizerSettings(),
                uiSettings = UiSettings(),
                privacySettings = PrivacySettings()
            )
        )

    init {
        loadAppInfo()
        loadCacheSize()
    }

    // Age Group Settings
    fun updateAgeGroup(ageGroup: AgeGroup) {
        viewModelScope.launch {
            userPreferencesManager.updateAgeGroup(ageGroup)
        }
    }

    // Theme Settings
    fun updateThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            userPreferencesManager.updateThemeMode(themeMode)
        }
    }

    // Playback Settings
    fun updateCrossfadeDuration(duration: Long) {
        viewModelScope.launch {
            userPreferencesManager.updateCrossfadeDuration(duration)
        }
    }

    fun updateGaplessPlayback(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesManager.updateGaplessPlayback(enabled)
        }
    }

    fun updateAutoPlayNext(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesManager.updateAutoPlayNext(enabled)
        }
    }

    // UI Settings
    fun updateAnimationIntensity(intensity: AnimationIntensity) {
        viewModelScope.launch {
            userPreferencesManager.updateAnimationIntensity(intensity)
        }
    }

    fun updateGridColumns(columns: Int) {
        viewModelScope.launch {
            userPreferencesManager.updateGridColumns(columns)
        }
    }

    fun updateSortBy(sortBy: SortBy) {
        viewModelScope.launch {
            userPreferencesManager.updateSortBy(sortBy)
        }
    }

    fun updateSortOrder(sortOrder: SortOrder) {
        viewModelScope.launch {
            userPreferencesManager.updateSortOrder(sortOrder)
        }
    }

    // Privacy Settings
    fun updateAllowAnalytics(allow: Boolean) {
        viewModelScope.launch {
            userPreferencesManager.updateAllowAnalytics(allow)
        }
    }

    fun updateAllowCrashReporting(allow: Boolean) {
        viewModelScope.launch {
            userPreferencesManager.updateAllowCrashReporting(allow)
        }
    }

    // Storage Management
    fun clearCache() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isClearing = true)

                // Clear cache implementation
                clearApplicationCache()

                // Update cache size
                loadCacheSize()

                _uiState.value = _uiState.value.copy(
                    isClearing = false,
                    message = "Đã xóa bộ nhớ đệm thành công"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isClearing = false,
                    message = "Lỗi khi xóa bộ nhớ đệm: ${e.message}"
                )
            }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isClearing = true)

                // Clear all data implementation
                clearAllApplicationData()

                _uiState.value = _uiState.value.copy(
                    isClearing = false,
                    message = "Đã xóa tất cả dữ liệu thành công"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isClearing = false,
                    message = "Lỗi khi xóa dữ liệu: ${e.message}"
                )
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }

    private fun loadAppInfo() {
        viewModelScope.launch {
            try {
                val packageInfo = getApplicationPackageInfo()
                _uiState.value = _uiState.value.copy(
                    appVersion = packageInfo.versionName ?: "Unknown",
                    buildNumber = packageInfo.longVersionCode.toString()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    appVersion = "Unknown",
                    buildNumber = "Unknown"
                )
            }
        }
    }

    private fun loadCacheSize() {
        viewModelScope.launch {
            try {
                val cacheSize = calculateCacheSize()
                _uiState.value = _uiState.value.copy(cacheSize = cacheSize)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(cacheSize = 0L)
            }
        }
    }

    private suspend fun clearApplicationCache() {
        // Implementation to clear app cache
        // This could include clearing image cache, database temp files, etc.
    }

    private suspend fun clearAllApplicationData() {
        // Implementation to clear all app data
        // This would reset preferences, clear database, etc.
    }

    private suspend fun getApplicationPackageInfo(): PackageInfo {
        // Implementation to get package info
        // Return mock data for now
        return PackageInfo().apply {
            versionName = "1.0.0"
            longVersionCode = 1L
        }
    }

    private suspend fun calculateCacheSize(): Long {
        // Implementation to calculate total cache size
        // This would sum up all cache directories
        return 0L
    }
}

data class SettingsUiState(
    val cacheSize: Long = 0L,
    val appVersion: String = "",
    val buildNumber: String = "",
    val isClearing: Boolean = false,
    val message: String? = null
)
