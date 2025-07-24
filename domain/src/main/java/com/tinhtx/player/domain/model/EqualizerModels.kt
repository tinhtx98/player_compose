package com.tinhtx.player.domain.model

data class EqualizerPreset(
    val id: String,
    val displayName: String,
    val levels: List<Short>
)

data class FrequencyBand(
    val index: Int,
    val centerFrequency: Int,
    val currentLevel: Short,
    val minLevel: Short,
    val maxLevel: Short
)

data class PackageInfo(
    val versionName: String,
    val longVersionCode: Long
)
