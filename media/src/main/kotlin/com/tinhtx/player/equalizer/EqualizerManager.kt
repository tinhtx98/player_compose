package com.tinhtx.player.equalizer

import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.Virtualizer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EqualizerManager @Inject constructor() {

    private var equalizer: Equalizer? = null
    private var bassBoost: BassBoost? = null
    private var virtualizer: Virtualizer? = null

    fun initialize(audioSessionId: Int) {
        try {
            equalizer = Equalizer(0, audioSessionId).apply {
                enabled = true
            }

            bassBoost = BassBoost(0, audioSessionId).apply {
                enabled = true
            }

            virtualizer = Virtualizer(0, audioSessionId).apply {
                enabled = true
            }
        } catch (e: Exception) {
            // Handle initialization error
            e.printStackTrace()
        }
    }

    fun setEqualizerEnabled(enabled: Boolean) {
        equalizer?.enabled = enabled
    }

    fun setBandLevel(band: Short, level: Short) {
        equalizer?.setBandLevel(band, level)
    }

    fun getBandLevel(band: Short): Short {
        return equalizer?.getBandLevel(band) ?: 0
    }

    fun getNumberOfBands(): Short {
        return equalizer?.numberOfBands ?: 0
    }

    fun getBandLevelRange(): ShortArray {
        return equalizer?.bandLevelRange ?: shortArrayOf(0, 0)
    }

    fun getCenterFreq(band: Short): Int {
        return equalizer?.getCenterFreq(band) ?: 0
    }

    fun setBassBoostStrength(strength: Short) {
        bassBoost?.setStrength(strength)
    }

    fun getBassBoostStrength(): Short {
        return bassBoost?.roundedStrength ?: 0
    }

    fun setVirtualizerStrength(strength: Short) {
        virtualizer?.setStrength(strength)
    }

    fun getVirtualizerStrength(): Short {
        return virtualizer?.roundedStrength ?: 0
    }

    fun setBassBoostEnabled(enabled: Boolean) {
        bassBoost?.enabled = enabled
    }

    fun setVirtualizerEnabled(enabled: Boolean) {
        virtualizer?.enabled = enabled
    }

    fun release() {
        equalizer?.release()
        bassBoost?.release()
        virtualizer?.release()

        equalizer = null
        bassBoost = null
        virtualizer = null
    }

    fun getPresetNames(): Array<String> {
        val numberOfPresets = equalizer?.numberOfPresets ?: 0
        return Array(numberOfPresets.toInt()) { index ->
            equalizer?.getPresetName(index.toShort()) ?: "Preset $index"
        }
    }

    fun usePreset(preset: Short) {
        equalizer?.usePreset(preset)
    }
}
