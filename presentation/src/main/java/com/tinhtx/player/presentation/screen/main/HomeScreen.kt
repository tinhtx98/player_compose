// presentation/src/main/kotlin/com/tinhtx/player/screen/main/HomeScreen.kt
package com.tinhtx.player.presentation.screen.main

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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tinhtx.player.common.Resource
import com.tinhtx.player.presentation.component.common.MediaItemCard
import com.tinhtx.player.presentation.component.common.SearchBar
import com.tinhtx.player.presentation.animation.WaterDropAnimation

@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToPlayer: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val userPreferences = viewModel.userPreferences.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
            QuickActionCard(title = "Yêu thích", subtitle = "${uiState.favoriteCount} bài") { viewModel.onShowFavorites() }
            QuickActionCard(title = "Gần đây", subtitle = "${uiState.recentCount} bài") { viewModel.onShowRecent() }
            QuickActionCard(title = "Playlist", subtitle = "${uiState.playlistCount} danh sách") { viewModel.onShowPlaylists() }
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (uiState.mediaItemsResource) {
            is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            is Resource.Success -> LazyColumn {
                items(uiState.mediaItemsResource.data.orEmpty()) { mediaItem ->
                    WaterDropAnimation(visible = true, ageGroup = userPreferences.ageGroup) {
                        MediaItemCard(
                            mediaItem = mediaItem,
                            onClick = { onNavigateToPlayer(mediaItem.id) },
                            onFavoriteClick = { viewModel.onToggleFavorite(mediaItem.id) },
                            onMoreClick = { viewModel.onShowMediaOptions(mediaItem.id) },
                            ageGroup = userPreferences.ageGroup
                        )
                    }
                }
            }
            is Resource.Error -> Text("Lỗi: ${uiState.mediaItemsResource.message}", color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun QuickActionCard(title: String, subtitle: String, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.size(100.dp)) {
        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}
