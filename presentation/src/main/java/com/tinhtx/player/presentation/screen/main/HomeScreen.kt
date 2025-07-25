package com.tinhtx.player.presentation.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tinhtx.player.core.common.Resource
import com.tinhtx.player.presentation.component.common.MediaItemCard
import com.tinhtx.player.presentation.component.common.SearchBar
import com.tinhtx.player.presentation.animation.WaterDropAnimation

@Composable
fun HomeScreen(
    permissionsGranted: Boolean,
    onNavigateToSearch: () -> Unit,
    onNavigateToPlayer: (String) -> Unit,
    onNavigateToVideoPlayer: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val userPreferences by viewModel.userPreferences.collectAsState()

    // Trigger quét media khi quyền được cấp
    LaunchedEffect(permissionsGranted) {
        if (permissionsGranted) {
            viewModel.refreshMediaLibrary()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                onSearchClick = onNavigateToSearch,
                placeholder = "Tìm kiếm nhạc, video...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Khám phá nhanh",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    QuickActionCard(
                        icon = Icons.Default.Favorite,
                        title = "Yêu thích",
                        subtitle = "${uiState.favoriteCount} bài",
                        onClick = { viewModel.onShowFavorites() }
                    )
                }
                item {
                    QuickActionCard(
                        icon = Icons.Default.History,
                        title = "Gần đây",
                        subtitle = "${uiState.recentCount} bài",
                        onClick = { viewModel.onShowRecent() }
                    )
                }
                item {
                    QuickActionCard(
                        icon = Icons.Default.PlaylistPlay,
                        title = "Playlist",
                        subtitle = "${uiState.playlistCount} danh sách",
                        onClick = { viewModel.onShowPlaylists() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Thư viện media",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (uiState.mediaItemsResource) {
                is Resource.Loading -> {
                    LoadingState()
                }

                is Resource.Success -> {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + scaleIn(spring(stiffness = 100f)),
                        exit = fadeOut()
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(160.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = uiState.mediaItemsResource.data ?: emptyList(),
                                key = { it.id }
                            ) { mediaItem ->
                                WaterDropAnimation(
                                    visible = true,
                                    ageGroup = userPreferences.ageGroup
                                ) {
                                    MediaItemCard(
                                        mediaItem = mediaItem,
                                        onClick = {
                                            when (mediaItem.type) {
                                                com.tinhtx.player.domain.model.MediaType.AUDIO -> {
                                                    onNavigateToPlayer(mediaItem.id)
                                                }
                                                com.tinhtx.player.domain.model.MediaType.VIDEO -> {
                                                    onNavigateToVideoPlayer(mediaItem.id)
                                                }
                                            }
                                        },
                                        onFavoriteClick = { viewModel.onToggleFavorite(mediaItem.id) },
                                        onMoreClick = { viewModel.onShowMediaOptions(mediaItem.id) },
                                        ageGroup = userPreferences.ageGroup
                                    )
                                }
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    ErrorState(
                        message = uiState.mediaItemsResource.message ?: "Vui lòng kiểm tra quyền truy cập hoặc thử quét lại",
                        onRetry = { viewModel.onRetry() }
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .width(120.dp)
            .height(80.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            strokeWidth = 4.dp
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Lỗi tải dữ liệu",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Thử lại")
            }
        }
    }
}
