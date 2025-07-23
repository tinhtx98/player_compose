package com.tinhtx.player.model

enum class MediaType {
    AUDIO,
    VIDEO
}

enum class RepeatMode {
    OFF,
    ONE,
    ALL
}

enum class AppTheme {
    LIGHT,
    DARK,
    SYSTEM
}

enum class SortOrder {
    TITLE_ASC,
    TITLE_DESC,
    ARTIST_ASC,
    ARTIST_DESC,
    ALBUM_ASC,
    ALBUM_DESC,
    DURATION_ASC,
    DURATION_DESC,
    DATE_ADDED_ASC,
    DATE_ADDED_DESC
}

enum class PlaybackError {
    NETWORK_ERROR,
    FILE_NOT_FOUND,
    PERMISSION_DENIED,
    UNSUPPORTED_FORMAT,
    UNKNOWN_ERROR
}
