package com.tinhtx.player.presentation.component.animation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.tinhtx.player.domain.model.AgeGroup
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun FallingLeavesAnimation(
    visible: Boolean,
    ageGroup: AgeGroup,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val animationSpeed = when (ageGroup) {
        AgeGroup.CHILD -> 2f
        AgeGroup.TEEN -> 1.5f
        AgeGroup.ADULT -> 1f
    }

    val leafCount = when (ageGroup) {
        AgeGroup.CHILD -> 15
        AgeGroup.TEEN -> 10
        AgeGroup.ADULT -> 5
    }

    // Get the color outside the Canvas context
    val leafColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)

    val infiniteTransition = rememberInfiniteTransition(label = "falling_leaves")
    val animationValue by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (3000 / animationSpeed).toInt(),
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "leaf_fall"
    )

    Box(modifier = modifier) {
        content()

        if (visible) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                repeat(leafCount) { index ->
                    drawLeaf(
                        drawScope = this,
                        progress = animationValue,
                        leafIndex = index,
                        totalLeaves = leafCount,
                        color = leafColor
                    )
                }
            }
        }
    }
}

@Composable
fun WaterWaveAnimation(
    visible: Boolean,
    ageGroup: AgeGroup,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val animationSpeed = when (ageGroup) {
        AgeGroup.CHILD -> 1.5f
        AgeGroup.TEEN -> 1.2f
        AgeGroup.ADULT -> 1f
    }

    val waveCount = when (ageGroup) {
        AgeGroup.CHILD -> 4
        AgeGroup.TEEN -> 3
        AgeGroup.ADULT -> 2
    }

    // Get the color outside the Canvas context
    val waveColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)

    val infiniteTransition = rememberInfiniteTransition(label = "water_waves")
    val waveProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (2000 / animationSpeed).toInt(),
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_progress"
    )

    Box(modifier = modifier) {
        content()

        if (visible) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                repeat(waveCount) { index ->
                    drawWave(
                        drawScope = this,
                        progress = waveProgress,
                        waveIndex = index,
                        totalWaves = waveCount,
                        color = waveColor
                    )
                }
            }
        }
    }
}

private fun drawLeaf(
    drawScope: DrawScope,
    progress: Float,
    leafIndex: Int,
    totalLeaves: Int,
    color: Color
) {
    val width = drawScope.size.width
    val height = drawScope.size.height

    val random = Random(leafIndex)
    val startX = random.nextFloat() * width
    val fallSpeed = 0.5f + random.nextFloat() * 0.5f
    val swayAmplitude = 20f + random.nextFloat() * 30f
    val swayFrequency = 1f + random.nextFloat() * 2f

    val y = (progress * height * fallSpeed) % (height + 50f)
    val swayOffset = sin(progress * swayFrequency * 2 * Math.PI) * swayAmplitude
    val x = startX + swayOffset.toFloat()

    val leafSize = 8f + random.nextFloat() * 6f
    val rotation = progress * 360f * (1f + random.nextFloat())

    drawScope.drawCircle(
        color = color,
        radius = leafSize,
        center = Offset(x, y)
    )
}

private fun drawWave(
    drawScope: DrawScope,
    progress: Float,
    waveIndex: Int,
    totalWaves: Int,
    color: Color
) {
    val width = drawScope.size.width
    val height = drawScope.size.height

    val waveHeight = height / 6f
    val waveLength = width / 2f
    val amplitude = 20f + (waveIndex * 10f)
    val frequency = 2f + (waveIndex * 0.5f)
    val phaseShift = progress * 2 * Math.PI + (waveIndex * Math.PI / totalWaves)

    val baseY = height - (waveHeight * (waveIndex + 1))

    for (x in 0..width.toInt() step 5) {
        val normalizedX = x / waveLength
        val y = baseY + sin(normalizedX * frequency + phaseShift) * amplitude

        drawScope.drawCircle(
            color = color,
            radius = 2f,
            center = Offset(x.toFloat(), y.toFloat())
        )
    }
}
