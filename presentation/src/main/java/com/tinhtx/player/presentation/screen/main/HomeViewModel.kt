// presentation/src/main/kotlin/com/tinhtx/player/screen/main/HomeViewModel.kt
package com.tinhtx.player.presentation.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinhtx.player.common.Resource
import com.tinhtx.player.data.local.preferences.UserPreferencesManager
import com.tinhtx.player.domain.model.MediaItem
import com.tinhtx.player.domain.model.UserPreferences
import com.tinhtx.player.domain.usecase.GetMediaItemsUseCase
import com.tinhtx.player.domain.usecase.ManagePlaylistUseCase
import com.tinhtx.player.domain.usecase.PlayMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMediaItemsUseCase: GetMediaItemsUseCase,
    private val playMediaUseCase: PlayMediaUseCase,
    private val managePlaylistUseCase: ManagePlaylistUseCase,
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val userPreferences: StateFlow<UserPreferences> = userPreferencesManager.getUserPreferences()
        .asStateFlow() // Assuming it's converted to StateFlow in manager

    init {
        loadMediaItems()
        loadStats()
    }

    private fun loadMediaItems() {
        viewModelScope.launch {
            getMediaItemsUseCase.getAllMediaItems()
                .map { Resource.Success(it) }
                .catch { emit(Resource.Error(it.message ?: "Lỗi không xác định")) }
                .collect { resource ->
                    _uiState.value = _uiState.value.copy(mediaItemsResource = resource)
                }
        }
    }

    private fun loadStats() {
        viewModelScope.launch {
            combine(
                getMediaItemsUseCase.getFavoriteItems(),
                getMediaItemsUseCase.getRecentlyPlayed(),
                managePlaylistUseCase.getAllPlaylists()
            ) { favorites, recent, playlists ->
                Triple(favorites.size, recent.size, playlists.size)
            }.collect { (favoriteCount, recentCount, playlistCount) ->
                _uiState.value = _uiState.value.copy(
                    favoriteCount = favoriteCount,
                    recentCount = recentCount,
                    playlistCount = playlistCount
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onToggleFavorite(mediaId: String) {
        viewModelScope.launch {
            playMediaUseCase.setFavorite(mediaId, true) // Example, toggle logic can be added
        }
    }

    fun onShowMediaOptions(mediaId: String) {
        // Logic to show options
    }

    fun onShowFavorites() {
        // Navigation logic
    }

    fun onShowRecent() {
        // Navigation logic
    }

    fun onShowPlaylists() {
        // Navigation logic
    }
}

data class HomeUiState(
    val mediaItemsResource: Resource<List<MediaItem>> = Resource.Loading(),
    val searchQuery: String = "",
    val favoriteCount: Int = 0,
    val recentCount: Int = 0,
    val playlistCount: Int = 0
)
