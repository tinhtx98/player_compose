// presentation/src/main/kotlin/com/tinhtx/player/screen/main/HomeScreen.kt
package com.tinhtx.player.presentation.screen.main

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        SearchBar(
            query = uiState.searchQuery,
            onQueryChange = viewModel::onSearchQueryChange,
            onSearchClick = onNavigateToSearch,
            placeholder = "Tìm kiếm nhạc, video...",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickActionCard(
                title = "Yêu thích",
                subtitle = "${uiState.favoriteCount} bài",
                onClick = { viewModel.onShowFavorites() }
            )
            QuickActionCard(
                title = "Gần đây",
                subtitle = "${uiState.recentCount} bài",
                onClick = { viewModel.onShowRecent() }
            )
            QuickActionCard(
                title = "Playlist",
                subtitle = "${uiState.playlistCount} danh sách",
                onClick = { viewModel.onShowPlaylists() }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (uiState.mediaItemsResource) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is Resource.Success -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
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
                                    // Navigate theo type của media
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

            is Resource.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Không tìm thấy media",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = uiState.mediaItemsResource.message ?: "Vui lòng kiểm tra quyền truy cập hoặc thử quét lại",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.onRetry() }
                        ) {
                            Text("Quét lại media")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .size(100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
