package com.tinhtx.player.common

object Constants {
    // Database
    const val DATABASE_NAME = "tinhtx_player_database"
    const val DATABASE_VERSION = 1

    // Preferences
    const val PREFERENCES_NAME = "tinhtx_player_preferences"

    // Media
    const val MEDIA_ROOT_ID = "media_root_id"
    const val NOTIFICATION_CHANNEL_ID = "tinhtx_player_channel"
    const val NOTIFICATION_ID = 1

    // Permission
    const val PERMISSION_REQUEST_CODE = 1001

    // Playback
    const val SEEK_BAR_UPDATE_INTERVAL = 1000L
    const val DEFAULT_MEDIA_VOLUME = 0.5f

    // Cache
    const val CACHE_SIZE = 100 * 1024 * 1024L // 100MB
}
