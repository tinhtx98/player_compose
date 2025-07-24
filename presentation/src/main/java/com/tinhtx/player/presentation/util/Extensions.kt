        package com.tinhtx.player.presentation.util

import kotlin.math.log10
import kotlin.math.pow

/**
 * Formats a byte size into a human-readable string
 */
fun Long.formatAsFileSize(): String {
    if (this <= 0) return "0 B"

    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt()

    return String.format(
        "%.1f %s",
        this / 1024.0.pow(digitGroups.toDouble()),
        units[digitGroups]
    )
}

/**
 * Formats duration in milliseconds to human-readable string
 */
fun Long.formatDuration(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}
