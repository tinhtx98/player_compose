// core/src/main/kotlin/com/tinhtx/player/common/Extensions.kt
package com.tinhtx.player.core.common

import java.util.concurrent.TimeUnit

fun Long.formatAsDuration(): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) % 60
    val hours = TimeUnit.MILLISECONDS.toHours(this)

    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%d:%02d", minutes, seconds)
    }
}

fun Long.formatAsFileSize(): String {
    val kb = this / 1024.0
    if (kb < 1024) {
        return String.format("%.1f KB", kb)
    }
    val mb = kb / 1024.0
    if (mb < 1024) {
        return String.format("%.1f MB", mb)
    }
    val gb = mb / 1024.0
    return String.format("%.1f GB", gb)
}
