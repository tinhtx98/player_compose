package com.tinhtx.player.presentation.screen.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@Composable
fun MiniPlayerBar(
    onNavigateToPlayer: () -> Unit,
    viewModel: PlaybackViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.playbackState.collectAsState()

    AnimatedVisibility(
        visible = state.currentItem != null,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        // Debug: Thêm log để kiểm tra state
        println("DEBUG: MiniPlayerBar rendering - currentItem: ${state.currentItem?.title}, isPlaying: ${state.isPlaying}")

        Surface(
            modifier = modifier
                .fillMaxWidth()
                .height(64.dp)
                .clickable { onNavigateToPlayer() },
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = state.currentItem?.albumArtUri ?: "",
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Column(modifier = Modifier.padding(start = 8.dp).weight(1f)) {
                    Text(text = state.currentItem?.title ?: "", style = MaterialTheme.typography.bodyMedium)
                    Text(text = state.currentItem?.artist ?: "", style = MaterialTheme.typography.bodySmall)
                }
                IconButton(onClick = { viewModel.togglePlayPause() }) {
                    Icon(
                        imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause"
                    )
                }
            }
            LinearProgressIndicator(
                progress = state.progress,
                modifier = Modifier.fillMaxWidth().height(2.dp)
            )
        }
    }
}
