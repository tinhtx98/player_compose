package com.tinhtx.player.common

import android.content.Context
import android.widget.Toast
import java.util.concurrent.TimeUnit

/**
 * Extension functions for common operations
 */

// Context extensions
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

// Long extensions for time formatting
fun Long.formatDuration(): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) % 60
    return String.format("%d:%02d", minutes, seconds)
}

// String extensions
fun String.isValidAudioFile(): Boolean {
    val audioExtensions = listOf("mp3", "wav", "flac", "aac", "ogg", "m4a")
    return audioExtensions.any { this.lowercase().endsWith(".$it") }
}

fun String.isValidVideoFile(): Boolean {
    val videoExtensions = listOf("mp4", "avi", "mkv", "mov", "wmv", "flv", "webm")
    return videoExtensions.any { this.lowercase().endsWith(".$it") }
}
