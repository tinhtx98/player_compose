package com.tinhtx.player.model

data class UserSettings(
    val theme: AppTheme = AppTheme.SYSTEM,
    val playbackSpeed: Float = 1.0f,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val shuffleMode: Boolean = false,
    val autoPlayNext: Boolean = true,
    val showNotification: Boolean = true,
    val equalizerEnabled: Boolean = false,
    val bassBoostLevel: Int = 0,
    val virtualizerLevel: Int = 0,
    val skipSilence: Boolean = false,
    val crossfadeDuration: Int = 0
)
