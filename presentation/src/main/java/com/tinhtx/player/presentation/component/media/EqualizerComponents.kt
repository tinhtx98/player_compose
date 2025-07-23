// presentation/src/main/kotlin/com/tinhtx/player/component/media/EqualizerComponents.kt
package com.tinhtx.player.presentation.component.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinhtx.player.domain.model.EqualizerPreset
import com.tinhtx.player.domain.model.FrequencyBand

@Composable
fun EqualizerBottomSheetContent(
    frequencyBands: List<FrequencyBand>,
    currentPreset: EqualizerPreset?,
    onApplyPreset: (EqualizerPreset) -> Unit,
    onSetBandLevel: (Int, Short) -> Unit,
    onSetBassBoost: (Short) -> Unit,
    onSetVirtualizer: (Short) -> Unit,
    bassBoostStrength: Short,
    virtualizerStrength: Short
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Equalizer",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(EqualizerPreset.values()) { preset ->
                FilterChip(
                    selected = currentPreset == preset,
                    onClick = { onApplyPreset(preset) },
                    label = { Text(preset.displayName) }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            frequencyBands.forEach { band ->
                EqualizerBandSlider(
                    band = band,
                    onLevelChange = { level ->
                        onSetBandLevel(band.index, level)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Bass Boost", fontWeight = FontWeight.Bold)
                Slider(
                    value = bassBoostStrength.toFloat(),
                    onValueChange = { onSetBassBoost(it.toShort()) },
                    valueRange = 0f..1000f,
                    modifier = Modifier.width(120.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Virtualizer", fontWeight = FontWeight.Bold)
                Slider(
                    value = virtualizerStrength.toFloat(),
                    onValueChange = { onSetVirtualizer(it.toShort()) },
                    valueRange = 0f..1000f,
                    modifier = Modifier.width(120.dp)
                )
            }
        }
    }
}

@Composable
private fun EqualizerBandSlider(
    band: FrequencyBand,
    onLevelChange: (Short) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(40.dp)
    ) {
        Text(
            text = formatFrequency(band.centerFrequency),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            fontSize = 10.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Slider(
            value = band.currentLevel.toFloat(),
            onValueChange = { onLevelChange(it.toShort()) },
            valueRange = band.minLevel.toFloat()..band.maxLevel.toFloat(),
            modifier = Modifier.height(200.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${band.currentLevel / 100}dB",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            fontSize = 10.sp
        )
    }
}

private fun formatFrequency(frequency: Int): String {
    return when {
        frequency < 1000 -> "${frequency}Hz"
        else -> "${frequency / 1000}kHz"
    }
}
