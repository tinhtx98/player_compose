// domain/src/main/kotlin/com/tinhtx/player/model/Enums.kt
package com.tinhtx.player.domain.model

enum class MediaType {
    AUDIO, VIDEO
}

enum class AgeGroup(val cornerRadius: Float, val baseFontSize: Float) {
    CHILD(cornerRadius = 32f, baseFontSize = 18f),
    TEEN(cornerRadius = 16f, baseFontSize = 16f),
    ADULT(cornerRadius = 8f, baseFontSize = 14f)
}

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

enum class AnimationIntensity {
    HIGH, MEDIUM, LOW, OFF
}

enum class SortBy {
    NAME, ARTIST, ALBUM, DATE_ADDED, DATE_MODIFIED, SIZE, DURATION
}

enum class SortOrder {
    ASCENDING, DESCENDING
}

enum class RepeatMode {
    OFF, ONE, ALL
}

enum class ShuffleMode {
    OFF, ON
}

enum class PlaybackMode {
    NORMAL, CROSSFADE, GAPLESS
}

enum class ReplayGainMode {
    OFF, TRACK, ALBUM
}

enum class ReverbPreset {
    NONE, SMALL_ROOM, MEDIUM_ROOM, LARGE_ROOM, MEDIUM_HALL, LARGE_HALL, PLATE
}
