package com.tinhtx.player.presentation.component.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tinhtx.player.domain.model.AgeGroup

@Composable
fun WaterWaveAnimation(
    visible: Boolean,
    ageGroup: AgeGroup,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        modifier = modifier
    ) {
        content()
    }
}

@Composable
fun FlowerBloomAnimation(
    visible: Boolean,
    ageGroup: AgeGroup,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        modifier = modifier
    ) {
        content()
    }
}

@Composable
fun FallingLeavesAnimation(
    visible: Boolean,
    ageGroup: AgeGroup,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        content()
    }
}
