// presentation/src/main/kotlin/com/tinhtx/player/screen/collection/CollectionViewModel.kt
package com.tinhtx.player.presentation.screen.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinhtx.player.core.common.Resource
import com.tinhtx.player.data.local.preferences.UserPreferencesManager
import com.tinhtx.player.domain.model.AlbumInfo
import com.tinhtx.player.domain.model.ArtistInfo
import com.tinhtx.player.domain.model.EqualizerSettings
import com.tinhtx.player.domain.model.PlaybackSettings
import com.tinhtx.player.domain.model.PlaylistInfo
import com.tinhtx.player.domain.model.PrivacySettings
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val getMediaItemsUseCase: GetMediaItemsUseCase,
    private val playMediaUseCase: PlayMediaUseCase,
    private val managePlaylistUseCase: ManagePlaylistUseCase,
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionUiState())
    val uiState: StateFlow<CollectionUiState> = _uiState.asStateFlow()

    val userPreferences: StateFlow<UserPreferences> = userPreferencesManager.getUserPreferences()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences(
                ageGroup = com.tinhtx.player.domain.model.AgeGroup.ADULT,
                themeMode = com.tinhtx.player.domain.model.ThemeMode.SYSTEM,
                primaryColor = null,
                language = "vi",
                playbackSettings = PlaybackSettings(),
                equalizerSettings = EqualizerSettings(),
                uiSettings = UiSettings(),
                privacySettings = PrivacySettings()
            )
        )

    init {
        loadCollections()
    }

    private fun loadCollections() {
        loadAlbums()
        loadArtists()
        loadGenres()
        loadPlaylists()
    }

    private fun loadAlbums() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(albumsResource = Resource.Loading())

            try {
                getMediaItemsUseCase.getAlbums().collect { albums ->
                    val sortedAlbums = sortAlbums(albums, userPreferences.value.uiSettings.sortBy, userPreferences.value.uiSettings.sortOrder)
                    _uiState.value = _uiState.value.copy(albumsResource = Resource.Success(sortedAlbums))
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(albumsResource = Resource.Error(e.message ?: "Lỗi khi tải albums"))
            }
        }
    }

    private fun loadArtists() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(artistsResource = Resource.Loading())

            try {
                getMediaItemsUseCase.getArtists().collect { artists ->
                    val sortedArtists = sortArtists(artists, userPreferences.value.uiSettings.sortBy, userPreferences.value.uiSettings.sortOrder)
                    _uiState.value = _uiState.value.copy(artistsResource = Resource.Success(sortedArtists))
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(artistsResource = Resource.Error(e.message ?: "Lỗi khi tải nghệ sĩ"))
            }
        }
    }

    private fun loadGenres() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(genresResource = Resource.Loading())

            try {
                getMediaItemsUseCase.getGenres().collect { genres ->
                    _uiState.value = _uiState.value.copy(genresResource = Resource.Success(genres.sorted()))
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(genresResource = Resource.Error(e.message ?: "Lỗi khi tải thể loại"))
            }
        }
    }

    private fun loadPlaylists() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(playlistsResource = Resource.Loading())

            try {
                managePlaylistUseCase.getAllPlaylists().collect { playlists ->
                    val sortedPlaylists = playlists.sortedByDescending { it.updatedAt }
                    _uiState.value = _uiState.value.copy(playlistsResource = Resource.Success(sortedPlaylists))
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(playlistsResource = Resource.Error(e.message ?: "Lỗi khi tải playlist"))
            }
        }
    }

    // Album Actions
    fun onAlbumClick(albumId: String) {
        _uiState.value = _uiState.value.copy(
            selectedItemId = albumId,
            selectedItemType = CollectionItemType.ALBUM
        )
        // Navigate to album detail or show bottom sheet
    }

    fun onPlayAlbum(albumId: String) {
        viewModelScope.launch {
            getMediaItemsUseCase.getMediaItemsByAlbum(albumId).collect { mediaItems ->
                if (mediaItems.isNotEmpty()) {
                    playMediaUseCase.playMediaItems(mediaItems, 0)
                }
            }
        }
    }

    // Artist Actions
    fun onArtistClick(artistId: String) {
        _uiState.value = _uiState.value.copy(
            selectedItemId = artistId,
            selectedItemType = CollectionItemType.ARTIST
        )
        // Navigate to artist detail or show bottom sheet
    }

    fun onPlayArtist(artistId: String) {
        viewModelScope.launch {
            getMediaItemsUseCase.getMediaItemsByArtist(artistId).collect { mediaItems ->
                if (mediaItems.isNotEmpty()) {
                    playMediaUseCase.playMediaItems(mediaItems, 0)
                }
            }
        }
    }

    // Genre Actions
    fun onGenreClick(genre: String) {
        _uiState.value = _uiState.value.copy(
            selectedItemId = genre,
            selectedItemType = CollectionItemType.GENRE
        )
        // Navigate to genre detail or show bottom sheet
    }

    fun onPlayGenre(genre: String) {
        viewModelScope.launch {
            getMediaItemsUseCase.getMediaItemsByGenre(genre).collect { mediaItems ->
                if (mediaItems.isNotEmpty()) {
                    playMediaUseCase.playMediaItems(mediaItems, 0)
                }
            }
        }
    }

    // Playlist Actions
    fun onPlaylistClick(playlistId: String) {
        _uiState.value = _uiState.value.copy(
            selectedItemId = playlistId,
            selectedItemType = CollectionItemType.PLAYLIST
        )
        // Navigate to playlist detail or show bottom sheet
    }

    fun onPlayPlaylist(playlistId: String) {
        viewModelScope.launch {
            managePlaylistUseCase.getPlaylistById(playlistId).collect { playlist ->
                playlist?.let {
                    if (it.tracks.isNotEmpty()) {
                        playMediaUseCase.playMediaItems(it.tracks, 0)
                    }
                }
            }
        }
    }

    fun showCreatePlaylistDialog() {
        _uiState.value = _uiState.value.copy(showCreatePlaylistDialog = true)
    }

    fun hideCreatePlaylistDialog() {
        _uiState.value = _uiState.value.copy(showCreatePlaylistDialog = false)
    }

    fun createPlaylist(name: String, description: String?) {
        viewModelScope.launch {
            try {
                managePlaylistUseCase.createPlaylist(name, description)
                _uiState.value = _uiState.value.copy(
                    showCreatePlaylistDialog = false,
                    message = "Đã tạo playlist '$name' thành công"
                )
                // Reload playlists
                loadPlaylists()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    message = "Lỗi khi tạo playlist: ${e.message}"
                )
            }
        }
    }

    // Sorting Functions
    private fun sortAlbums(albums: List<AlbumInfo>, sortBy: SortBy, sortOrder: SortOrder): List<AlbumInfo> {
        val sorted = when (sortBy) {
            SortBy.NAME -> albums.sortedBy { it.name }
            SortBy.ARTIST -> albums.sortedBy { it.artist }
            SortBy.DATE_ADDED -> albums.sortedBy { it.year ?: 0 }
            else -> albums.sortedBy { it.name }
        }

        return when (sortOrder) {
            SortOrder.ASCENDING -> sorted
            SortOrder.DESCENDING -> sorted.reversed()
        }
    }

    private fun sortArtists(artists: List<ArtistInfo>, sortBy: SortBy, sortOrder: SortOrder): List<ArtistInfo> {
        val sorted = when (sortBy) {
            SortBy.NAME -> artists.sortedBy { it.name }
            else -> artists.sortedBy { it.name }
        }

        return when (sortOrder) {
            SortOrder.ASCENDING -> sorted
            SortOrder.DESCENDING -> sorted.reversed()
        }
    }

    // Filter Functions
    fun filterAlbumsByYear(startYear: Int?, endYear: Int?) {
        viewModelScope.launch {
            val currentAlbums = _uiState.value.albumsResource.data ?: return@launch

            val filteredAlbums = currentAlbums.filter { album ->
                val year = album.year
                when {
                    startYear != null && endYear != null -> year != null && year in startYear..endYear
                    startYear != null -> year != null && year >= startYear
                    endYear != null -> year != null && year <= endYear
                    else -> true
                }
            }

            _uiState.value = _uiState.value.copy(
                albumsResource = Resource.Success(filteredAlbums)
            )
        }
    }

    fun resetFilters() {
        loadCollections()
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(
            selectedItemId = null,
            selectedItemType = null
        )
    }
}

data class CollectionUiState(
    val albumsResource: Resource<List<AlbumInfo>> = Resource.Loading(),
    val artistsResource: Resource<List<ArtistInfo>> = Resource.Loading(),
    val genresResource: Resource<List<String>> = Resource.Loading(),
    val playlistsResource: Resource<List<PlaylistInfo>> = Resource.Loading(),
    val selectedItemId: String? = null,
    val selectedItemType: CollectionItemType? = null,
    val showCreatePlaylistDialog: Boolean = false,
    val message: String? = null,
    val isLoading: Boolean = false
)

enum class CollectionItemType {
    ALBUM, ARTIST, GENRE, PLAYLIST
}
