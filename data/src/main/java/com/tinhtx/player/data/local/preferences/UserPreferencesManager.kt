// data/src/main/kotlin/com/tinhtx/player/local/preferences/UserPreferencesManager.kt
package com.tinhtx.player.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tinhtx.player.domain.model.AgeGroup
import com.tinhtx.player.domain.model.AnimationIntensity
import com.tinhtx.player.domain.model.PlaybackSettings
import com.tinhtx.player.domain.model.PrivacySettings
import com.tinhtx.player.domain.model.ReplayGainMode
import com.tinhtx.player.domain.model.ReverbPreset
import com.tinhtx.player.domain.model.SortBy
import com.tinhtx.player.domain.model.SortOrder
import com.tinhtx.player.domain.model.ThemeMode
import com.tinhtx.player.domain.model.UiSettings
import com.tinhtx.player.domain.model.UserPreferences
import com.tinhtx.player.domain.model.EqualizerSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // Keys for DataStore
    private object PreferenceKeys {
        val AGE_GROUP = stringPreferencesKey("age_group")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        // Playback Settings
        val CROSSFADE_DURATION = longPreferencesKey("crossfade_duration")
        val GAPLESS_PLAYBACK = booleanPreferencesKey("gapless_playback")
        val AUTO_PLAY_NEXT = booleanPreferencesKey("auto_play_next")
        // UI Settings
        val ANIMATION_INTENSITY = stringPreferencesKey("animation_intensity")
        val GRID_COLUMNS = intPreferencesKey("grid_columns")
        val SORT_BY = stringPreferencesKey("sort_by")
        val SORT_ORDER = stringPreferencesKey("sort_order")
        // Privacy Settings
        val ALLOW_ANALYTICS = booleanPreferencesKey("allow_analytics")
        val ALLOW_CRASH_REPORTING = booleanPreferencesKey("allow_crash_reporting")
    }

    fun getUserPreferences(): Flow<UserPreferences> {
        return context.dataStore.data.map { preferences ->
            UserPreferences(
                ageGroup = preferences[PreferenceKeys.AGE_GROUP]?.let {
                    AgeGroup.valueOf(it)
                } ?: AgeGroup.ADULT,
                themeMode = preferences[PreferenceKeys.THEME_MODE]?.let {
                    ThemeMode.valueOf(it)
                } ?: ThemeMode.SYSTEM,
                primaryColor = null, // Will be implemented with dynamic colors
                language = "vi",
                playbackSettings = PlaybackSettings(
                    crossfadeDuration = preferences[PreferenceKeys.CROSSFADE_DURATION] ?: 3000L,
                    gaplessPlayback = preferences[PreferenceKeys.GAPLESS_PLAYBACK] ?: false,
                    autoPlayNext = preferences[PreferenceKeys.AUTO_PLAY_NEXT] ?: true,
                    stopAfterCurrentTrack = false,
                    audioFocusHandling = true,
                    resumeOnHeadsetConnect = true,
                    pauseOnHeadsetDisconnect = true,
                    skipSilence = false,
                    normalizeVolume = false,
                    replayGain = ReplayGainMode.OFF
                ),
                equalizerSettings = EqualizerSettings(
                    isEnabled = false,
                    preset = null,
                    bands = emptyList(),
                    bassBoost = 0f,
                    virtualizer = 0f,
                    reverb = ReverbPreset.NONE
                ),
                uiSettings = UiSettings(
                    showMiniPlayer = true,
                    showLyrics = true,
                    showVisualizer = true,
                    animationIntensity = preferences[PreferenceKeys.ANIMATION_INTENSITY]?.let {
                        AnimationIntensity.valueOf(it)
                    } ?: AnimationIntensity.MEDIUM,
                    gridColumns = preferences[PreferenceKeys.GRID_COLUMNS] ?: 2,
                    sortBy = preferences[PreferenceKeys.SORT_BY]?.let {
                        SortBy.valueOf(it)
                    } ?: SortBy.NAME,
                    sortOrder = preferences[PreferenceKeys.SORT_ORDER]?.let {
                        SortOrder.valueOf(it)
                    } ?: SortOrder.ASCENDING,
                    showFileExtensions = false,
                    showDurations = true,
                    showArtwork = true
                ),
                privacySettings = PrivacySettings(
                    allowAnalytics = preferences[PreferenceKeys.ALLOW_ANALYTICS] ?: false,
                    allowCrashReporting = preferences[PreferenceKeys.ALLOW_CRASH_REPORTING] ?: false,
                    allowUsageStats = false,
                    allowLocationAccess = false,
                    clearDataOnUninstall = true
                )
            )
        }
    }

    suspend fun updateAgeGroup(ageGroup: AgeGroup) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.AGE_GROUP] = ageGroup.name
        }
    }

    suspend fun updateThemeMode(themeMode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.THEME_MODE] = themeMode.name
        }
    }

    suspend fun updateAnimationIntensity(intensity: AnimationIntensity) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.ANIMATION_INTENSITY] = intensity.name
        }
    }

    suspend fun updateCrossfadeDuration(duration: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.CROSSFADE_DURATION] = duration
        }
    }

    suspend fun updateGaplessPlayback(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.GAPLESS_PLAYBACK] = enabled
        }
    }

    suspend fun updateAutoPlayNext(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.AUTO_PLAY_NEXT] = enabled
        }
    }

    suspend fun updateGridColumns(columns: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.GRID_COLUMNS] = columns
        }
    }

    suspend fun updateSortBy(sortBy: SortBy) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.SORT_BY] = sortBy.name
        }
    }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateAllowAnalytics(allow: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.ALLOW_ANALYTICS] = allow
        }
    }

    suspend fun updateAllowCrashReporting(allow: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.ALLOW_CRASH_REPORTING] = allow
        }
    }
}
