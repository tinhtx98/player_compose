// presentation/src/main/kotlin/com/tinhtx/player/screen/main/HomeViewModel.kt
package com.tinhtx.player.presentation.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinhtx.player.common.Resource
import com.tinhtx.player.domain.model.AnimationIntensity
import com.tinhtx.player.domain.model.EqualizerSettings
import com.tinhtx.player.domain.model.MediaItem
import com.tinhtx.player.domain.model.PlaybackSettings
import com.tinhtx.player.domain.model.PrivacySettings
import com.tinhtx.player.domain.model.ReplayGainMode
import com.tinhtx.player.domain.model.ReverbPreset
import com.tinhtx.player.domain.model.SortBy
import com.tinhtx.player.domain.model.SortOrder
import com.tinhtx.player.domain.model.UiSettings
import com.tinhtx.player.domain.model.UserPreferences
import com.tinhtx.player.domain.usecase.GetMediaItemsUseCase
import com.tinhtx.player.domain.usecase.ManagePlaylistUseCase
import com.tinhtx.player.domain.usecase.PlayMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences(
                ageGroup = com.tinhtx.player.domain.model.AgeGroup.ADULT,
                themeMode = com.tinhtx.player.domain.model.ThemeMode.SYSTEM,
                primaryColor = null,
                language = "vi",
                playbackSettings = PlaybackSettings(
                    crossfadeDuration = 3000L,
                    gaplessPlayback = false,
                    autoPlayNext = true,
                    stopAfterCurrentTrack = false,
                    audioFocusHandling = true,
                    resumeOnHeadsetConnect = true,
                    pauseOnHeadsetDisconnect = true,
                    skipSilence = false,
                    normalizeVolume = false,
                    replayGain = ReplayGainMode.OFF
                ),
                equalizerSettings = EqualizerSettings(
                    isEnabled = false,
                    preset = null,
                    bands = emptyList(),
                    bassBoost = 0f,
                    virtualizer = 0f,
                    reverb = ReverbPreset.NONE
                ),
                uiSettings = UiSettings(
                    showMiniPlayer = true,
                    showLyrics = true,
                    showVisualizer = true,
                    animationIntensity = AnimationIntensity.MEDIUM,
                    gridColumns = 2,
                    sortBy = SortBy.NAME,
                    sortOrder = SortOrder.ASCENDING,
                    showFileExtensions = false,
                    showDurations = true,
                    showArtwork = true
                ),
                privacySettings = PrivacySettings(
                    allowAnalytics = false,
                    allowCrashReporting = false,
                    allowUsageStats = false,
                    allowLocationAccess = false,
                    clearDataOnUninstall = true
                )
            )
        )

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
                    _uiState.value = _uiState.value.copy(
                        mediaItemsResource = resource
                    )
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
            val currentItem = _uiState.value.mediaItemsResource.data?.find { it.id == mediaId }
            if (currentItem != null) {
                playMediaUseCase.setFavorite(mediaId, !currentItem.isFavorite)
            }
        }
    }

    fun onShowMediaOptions(mediaId: String) {
        _uiState.value = _uiState.value.copy(
            selectedMediaId = mediaId,
            showMediaOptions = true
        )
    }

    fun onShowFavorites() {
        // Navigation to favorites screen
    }

    fun onShowRecent() {
        // Navigation to recent screen
    }

    fun onShowPlaylists() {
        // Navigation to playlists screen
    }

    fun onRetry() {
        loadMediaItems()
    }
}

data class HomeUiState(
    val mediaItemsResource: Resource<List<MediaItem>> = Resource.Loading(),
    val searchQuery: String = "",
    favoriteCount: Int = 0,
    recentCount: Int = 0,
    playlistCount: Int = 0,
    selectedMediaId: String? = null,
    showMediaOptions: Boolean = false,
    isRefreshing: Boolean = false
)
