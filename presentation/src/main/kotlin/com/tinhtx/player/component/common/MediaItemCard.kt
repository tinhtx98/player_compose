package com.tinhtx.player.component.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tinhtx.player.model.MediaItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaItemCard(
    mediaItem: MediaItem,
    onClick: (MediaItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onClick(mediaItem) },
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album artwork
            AsyncImage(
                model = mediaItem.artworkUri,
                contentDescription = "Album artwork",
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Media info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = mediaItem.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${mediaItem.artist} • ${mediaItem.album}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Duration
            Text(
                text = formatDuration(mediaItem.duration),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

private fun formatDuration(duration: Long): String {
    val minutes = duration / 1000 / 60
    val seconds = (duration / 1000) % 60
    return String.format("%d:%02d", minutes, seconds)
}
