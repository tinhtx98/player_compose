package com.tinhtx.player.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinhtx.player.local.preferences.UserPreferencesManager
import com.tinhtx.player.model.AppTheme
import com.tinhtx.player.model.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _userSettings = MutableStateFlow(UserSettings())
    val userSettings: StateFlow<UserSettings> = _userSettings.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        _userSettings.value = userPreferencesManager.getUserSettings()
    }

    fun updateTheme(theme: AppTheme) {
        val updatedSettings = _userSettings.value.copy(theme = theme)
        _userSettings.value = updatedSettings
        saveSettings(updatedSettings)
    }

    fun updateSettings(settings: UserSettings) {
        _userSettings.value = settings
        saveSettings(settings)
    }

    fun updateBassBoost(level: Int) {
        val updatedSettings = _userSettings.value.copy(bassBoostLevel = level)
        _userSettings.value = updatedSettings
        saveSettings(updatedSettings)
    }

    fun updateVirtualizer(level: Int) {
        val updatedSettings = _userSettings.value.copy(virtualizerLevel = level)
        _userSettings.value = updatedSettings
        saveSettings(updatedSettings)
    }

    private fun saveSettings(settings: UserSettings) {
        viewModelScope.launch {
            userPreferencesManager.saveUserSettings(settings)
        }
    }
}
