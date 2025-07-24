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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinhtx.player.domain.model.EqualizerPreset
import com.tinhtx.player.domain.model.FrequencyBand

@Composable
fun EqualizerBottomSheetContent(
    frequencyBands: List<FrequencyBand>,
    currentPreset: EqualizerPreset?,
    presets: List<EqualizerPreset>,
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
            items(presets.size) { index ->
                val preset = presets[index]
                FilterChip(
                    selected = currentPreset?.id == preset.id,
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
            frequencyBands.forEachIndexed { index, band ->
                EqualizerBandSlider(
                    band = band,
                    onValueChange = { value ->
                        onSetBandLevel(index, value.toInt().toShort())
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
                    onValueChange = { onSetBassBoost(it.toInt().toShort()) },
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
                    onValueChange = { onSetVirtualizer(it.toInt().toShort()) },
                    valueRange = 0f..1000f,
                    modifier = Modifier.width(120.dp)
                )
            }
        }
    }
}

@Composable
fun EqualizerBandSlider(
    band: FrequencyBand,
    onValueChange: (Float) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(48.dp)
    ) {
        Text(
            text = "${band.centerFrequency}Hz",
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )

        Slider(
            value = band.currentLevel.toFloat(),
            onValueChange = onValueChange,
            valueRange = band.minLevel.toFloat()..band.maxLevel.toFloat(),
            modifier = Modifier.height(120.dp)
        )

        Text(
            text = "${band.currentLevel}dB",
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}
