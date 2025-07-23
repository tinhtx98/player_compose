// domain/src/main/kotlin/com/tinhtx/player/model/UserSettings.kt
package com.tinhtx.player.domain.model

data class UserPreferences(
    val ageGroup: AgeGroup,
    val themeMode: ThemeMode,
    val primaryColor: Int?,
    val language: String,
    val playbackSettings: PlaybackSettings,
    val equalizerSettings: EqualizerSettings,
    val uiSettings: UiSettings,
    val privacySettings: PrivacySettings
)

data class PlaybackSettings(
    val crossfadeDuration: Long = 3000L,
    val gaplessPlayback: Boolean = false,
    val autoPlayNext: Boolean = true,
    val stopAfterCurrentTrack: Boolean = false,
    val audioFocusHandling: Boolean = true,
    val resumeOnHeadsetConnect: Boolean = true,
    val pauseOnHeadsetDisconnect: Boolean = true,
    val skipSilence: Boolean = false,
    val normalizeVolume: Boolean = false,
    val replayGain: ReplayGainMode = ReplayGainMode.OFF
)

data class EqualizerSettings(
    val isEnabled: Boolean = false,
    val preset: String? = null,
    val bands: List<Float> = emptyList(),
    val bassBoost: Float = 0f,
    val virtualizer: Float = 0f,
    val reverb: ReverbPreset = ReverbPreset.NONE
)

data class UiSettings(
    val showMiniPlayer: Boolean = true,
    val showLyrics: Boolean = true,
    val showVisualizer: Boolean = true,
    val animationIntensity: AnimationIntensity = AnimationIntensity.MEDIUM,
    val gridColumns: Int = 2,
    val sortBy: SortBy = SortBy.NAME,
    val sortOrder: SortOrder = SortOrder.ASCENDING,
    val showFileExtensions: Boolean = false,
    val showDurations: Boolean = true,
    val showArtwork: Boolean = true
)

data class PrivacySettings(
    val allowAnalytics: Boolean = false,
    val allowCrashReporting: Boolean = false,
    val allowUsageStats: Boolean = false,
    val allowLocationAccess: Boolean = false,
    val clearDataOnUninstall: Boolean = true
)
