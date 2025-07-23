package com.tinhtx.player.local.preferences

import android.content.Context
import android.content.SharedPreferences
import com.tinhtx.player.model.AppTheme
import com.tinhtx.player.model.RepeatMode
import com.tinhtx.player.model.UserSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesManager @Inject constructor(
    context: Context
) {
    private val preferences: SharedPreferences = context.getSharedPreferences(
        "tinhtx_player_preferences",
        Context.MODE_PRIVATE
    )

    fun getUserSettings(): UserSettings {
        return UserSettings(
            theme = AppTheme.valueOf(preferences.getString(KEY_THEME, AppTheme.SYSTEM.name) ?: AppTheme.SYSTEM.name),
            playbackSpeed = preferences.getFloat(KEY_PLAYBACK_SPEED, 1.0f),
            repeatMode = RepeatMode.valueOf(preferences.getString(KEY_REPEAT_MODE, RepeatMode.OFF.name) ?: RepeatMode.OFF.name),
            shuffleMode = preferences.getBoolean(KEY_SHUFFLE_MODE, false),
            autoPlayNext = preferences.getBoolean(KEY_AUTO_PLAY_NEXT, true),
            showNotification = preferences.getBoolean(KEY_SHOW_NOTIFICATION, true),
            equalizerEnabled = preferences.getBoolean(KEY_EQUALIZER_ENABLED, false),
            bassBoostLevel = preferences.getInt(KEY_BASS_BOOST, 0),
            virtualizerLevel = preferences.getInt(KEY_VIRTUALIZER, 0),
            skipSilence = preferences.getBoolean(KEY_SKIP_SILENCE, false),
            crossfadeDuration = preferences.getInt(KEY_CROSSFADE_DURATION, 0)
        )
    }

    fun saveUserSettings(settings: UserSettings) {
        preferences.edit().apply {
            putString(KEY_THEME, settings.theme.name)
            putFloat(KEY_PLAYBACK_SPEED, settings.playbackSpeed)
            putString(KEY_REPEAT_MODE, settings.repeatMode.name)
            putBoolean(KEY_SHUFFLE_MODE, settings.shuffleMode)
            putBoolean(KEY_AUTO_PLAY_NEXT, settings.autoPlayNext)
            putBoolean(KEY_SHOW_NOTIFICATION, settings.showNotification)
            putBoolean(KEY_EQUALIZER_ENABLED, settings.equalizerEnabled)
            putInt(KEY_BASS_BOOST, settings.bassBoostLevel)
            putInt(KEY_VIRTUALIZER, settings.virtualizerLevel)
            putBoolean(KEY_SKIP_SILENCE, settings.skipSilence)
            putInt(KEY_CROSSFADE_DURATION, settings.crossfadeDuration)
            apply()
        }
    }

    companion object {
        private const val KEY_THEME = "theme"
        private const val KEY_PLAYBACK_SPEED = "playback_speed"
        private const val KEY_REPEAT_MODE = "repeat_mode"
        private const val KEY_SHUFFLE_MODE = "shuffle_mode"
        private const val KEY_AUTO_PLAY_NEXT = "auto_play_next"
        private const val KEY_SHOW_NOTIFICATION = "show_notification"
        private const val KEY_EQUALIZER_ENABLED = "equalizer_enabled"
        private const val KEY_BASS_BOOST = "bass_boost"
        private const val KEY_VIRTUALIZER = "virtualizer"
        private const val KEY_SKIP_SILENCE = "skip_silence"
        private const val KEY_CROSSFADE_DURATION = "crossfade_duration"
    }
}
