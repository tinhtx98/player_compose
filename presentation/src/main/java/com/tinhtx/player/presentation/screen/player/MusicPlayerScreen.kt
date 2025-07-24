// presentation/src/main/kotlin/com/tinhtx/player/screen/player/MusicPlayerScreen.kt
package com.tinhtx.player.presentation.screen.player

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode as AnimationRepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lyrics
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tinhtx.player.domain.model.RepeatMode as PlaybackRepeatMode
import com.tinhtx.player.domain.model.ShuffleMode
import com.tinhtx.player.presentation.R
import com.tinhtx.player.presentation.component.media.*
import com.tinhtx.player.presentation.component.animation.FallingLeavesAnimation
import com.tinhtx.player.presentation.component.animation.WaterWaveAnimation
import com.tinhtx.player.presentation.component.animation.FlowerBloomAnimation
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerScreen(
    mediaId: String,
    onNavigateBack: () -> Unit,
    viewModel: MusicPlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userPreferences by viewModel.userPreferences.collectAsStateWithLifecycle()
    val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()

    var showEqualizer by remember { mutableStateOf(false) }
    var showLyrics by remember { mutableStateOf(false) }

    // Animation states
    val albumArtRotation by animateFloatAsState(
        targetValue = if (playbackState.isPlaying) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = AnimationRepeatMode.Restart
        ),
        label = "Album Art Rotation"
    )

    val pulseScale by animateFloatAsState(
        targetValue = if (playbackState.isPlaying) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = AnimationRepeatMode.Reverse
        ),
        label = "Pulse Scale"
    )

    LaunchedEffect(mediaId) {
        viewModel.loadAndPlayMediaItem(mediaId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar with Back Button
            WaterWaveAnimation(
                visible = true,
                ageGroup = userPreferences.ageGroup
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Text(
                        text = "Now Playing",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    IconButton(onClick = { /* More options */ }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Album Art Section
            FlowerBloomAnimation(
                visible = uiState.currentMediaItem != null,
                ageGroup = userPreferences.ageGroup
            ) {
                Box(
                    modifier = Modifier
                        .size(320.dp)
                        .clip(RoundedCornerShape(userPreferences.ageGroup.cornerRadius))
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
                                )
                            )
                        )
                        .scale(pulseScale)
                        .rotate(albumArtRotation),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = uiState.currentMediaItem?.albumArtUri,
                        contentDescription = "Album Art",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(userPreferences.ageGroup.cornerRadius)),
                        contentScale = ContentScale.Crop,
                        error = painterResource(R.drawable.ic_music_note),
                        placeholder = painterResource(R.drawable.ic_music_note)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Track Info
            WaterWaveAnimation(
                visible = uiState.currentMediaItem != null,
                ageGroup = userPreferences.ageGroup
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = uiState.currentMediaItem?.title ?: "Unknown Title",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = uiState.currentMediaItem?.artist ?: "Unknown Artist",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Progress Bar
            FallingLeavesAnimation(
                visible = true,
                ageGroup = userPreferences.ageGroup
            ) {
                Column {
                    Slider(
                        value = playbackState.playbackPosition.toFloat(),
                        onValueChange = { viewModel.seekTo(it.toLong()) },
                        valueRange = 0f..playbackState.duration.toFloat(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatTime(playbackState.playbackPosition),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )

                        Text(
                            text = formatTime(playbackState.duration),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Playback Controls
            WaterWaveAnimation(
                visible = true,
                ageGroup = userPreferences.ageGroup
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.toggleShuffle() },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.Shuffle,
                            contentDescription = "Shuffle",
                            tint = if (playbackState.shuffleMode == ShuffleMode.ON)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.skipToPrevious() },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            Icons.Default.SkipPrevious,
                            contentDescription = "Previous",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    // Play/Pause Button with Animation
                    FloatingActionButton(
                        onClick = { viewModel.togglePlayPause() },
                        modifier = Modifier.size(72.dp),
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        AnimatedContent(
                            targetState = playbackState.isPlaying,
                            transitionSpec = {
                                scaleIn(
                                    animationSpec = tween(150, easing = FastOutSlowInEasing)
                                ) togetherWith scaleOut(
                                    animationSpec = tween(150, easing = FastOutSlowInEasing)
                                )
                            },
                            label = "Play/Pause Icon"
                        ) { isPlaying ->
                            Icon(
                                if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = { viewModel.skipToNext() },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            Icons.Default.SkipNext,
                            contentDescription = "Next",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    IconButton(
                        onClick = { viewModel.toggleRepeat() },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            when (playbackState.repeatMode) {
                                PlaybackRepeatMode.ONE -> Icons.Default.RepeatOne
                                PlaybackRepeatMode.ALL -> Icons.Default.Repeat
                                else -> Icons.Default.Repeat
                            },
                            contentDescription = "Repeat",
                            tint = if (playbackState.repeatMode != PlaybackRepeatMode.OFF)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Additional Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { showEqualizer = true }) {
                    Icon(
                        Icons.Default.Equalizer,
                        contentDescription = "Equalizer",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }

                IconButton(
                    onClick = { showLyrics = !showLyrics },
                    enabled = !uiState.lyrics.isNullOrBlank()
                ) {
                    Icon(
                        Icons.Default.Lyrics,
                        contentDescription = "Lyrics",
                        tint = if (!uiState.lyrics.isNullOrBlank())
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        else
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                    )
                }

                IconButton(onClick = { viewModel.toggleFavorite() }) {
                    Icon(
                        if (uiState.currentMediaItem?.isFavorite == true)
                            Icons.Default.Favorite
                        else
                            Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (uiState.currentMediaItem?.isFavorite == true)
                            Color.Red
                        else
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }

    // Equalizer Bottom Sheet
    if (showEqualizer) {
        EqualizerBottomSheet(
            visible = true,
            onDismiss = { showEqualizer = false }
        ) {
            // Equalizer content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Equalizer",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                // Add equalizer controls here
                Text(
                    text = "Equalizer controls will be implemented here",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    // Lyrics Panel
    if (showLyrics) {
        LyricsPanel(
            visible = true,
            onDismiss = { showLyrics = false },
            currentPosition = playbackState.playbackPosition,
            lyrics = uiState.lyrics
        )
    }
}

@Composable
private fun formatTime(milliseconds: Long): String {
    val seconds = (milliseconds / 1000) % 60
    val minutes = (milliseconds / (1000 * 60)) % 60
    val hours = (milliseconds / (1000 * 60 * 60)) % 24

    return if (hours > 0) {
        String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}
