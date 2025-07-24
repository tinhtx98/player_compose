// presentation/src/main/kotlin/com/tinhtx/player/screen/search/SearchViewModel.kt
package com.tinhtx.player.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinhtx.player.core.common.Resource
import com.tinhtx.player.data.local.preferences.UserPreferencesManager
import com.tinhtx.player.domain.model.EqualizerSettings
import com.tinhtx.player.domain.model.MediaItem
import com.tinhtx.player.domain.model.MediaType
import com.tinhtx.player.domain.model.PlaybackSettings
import com.tinhtx.player.domain.model.PrivacySettings
import com.tinhtx.player.domain.model.UiSettings
import com.tinhtx.player.domain.model.UserPreferences
import com.tinhtx.player.domain.usecase.GetMediaItemsUseCase
import com.tinhtx.player.domain.usecase.PlayMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getMediaItemsUseCase: GetMediaItemsUseCase,
    private val playMediaUseCase: PlayMediaUseCase,
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

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

    private val searchQueryFlow = MutableStateFlow("")

    init {
        // Auto search with debounce
        viewModelScope.launch {
            searchQueryFlow
                .debounce(300)
                .distinctUntilChanged()
                .filter { it.isNotEmpty() }
                .collect { query ->
                    performSearchInternal(query)
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        searchQueryFlow.value = query

        if (query.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                searchResults = Resource.Success(emptyList())
            )
        }
    }

    fun performSearch() {
        val query = _uiState.value.searchQuery
        if (query.isNotEmpty()) {
            viewModelScope.launch {
                performSearchInternal(query)
            }
        }
    }

    private suspend fun performSearchInternal(query: String) {
        if (query.isEmpty()) return

        _uiState.value = _uiState.value.copy(searchResults = Resource.Loading())

        try {
            getMediaItemsUseCase.searchMediaItems(query).collect { items ->
                // Apply filter if selected
                val filteredItems = _uiState.value.selectedFilter?.let { filter ->
                    items.filter { it.type == filter }
                } ?: items

                _uiState.value = _uiState.value.copy(searchResults = Resource.Success(filteredItems))
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                searchResults = Resource.Error(e.message ?: "Lỗi không xác định")
            )
        }
    }

    fun setFilter(mediaType: MediaType?) {
        _uiState.value = _uiState.value.copy(selectedFilter = mediaType)

        // Re-perform search with new filter
        if (_uiState.value.searchQuery.isNotEmpty()) {
            viewModelScope.launch {
                performSearchInternal(_uiState.value.searchQuery)
            }
        }
    }

    fun loadSearchHistory() {
        viewModelScope.launch {
            // Load from preferences or database
            val history = getSearchHistoryFromStorage()
            _uiState.value = _uiState.value.copy(searchHistory = history)
        }
    }

    fun addToHistory(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            val currentHistory = _uiState.value.searchHistory.toMutableList()

            // Remove if already exists to move to top
            currentHistory.remove(query)

            // Add to beginning
            currentHistory.add(0, query)

            // Keep only last 10 items
            val updatedHistory = currentHistory.take(10)

            _uiState.value = _uiState.value.copy(searchHistory = updatedHistory)

            // Save to storage
            saveSearchHistoryToStorage(updatedHistory)
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(searchHistory = emptyList())
            saveSearchHistoryToStorage(emptyList())
        }
    }

    fun toggleFavorite(mediaId: String) {
        viewModelScope.launch {
            val currentResults = _uiState.value.searchResults.data ?: return@launch
            val mediaItem = currentResults.find { it.id == mediaId } ?: return@launch

            playMediaUseCase.setFavorite(mediaId, !mediaItem.isFavorite)

            // Update local state
            val updatedResults = currentResults.map { item ->
                if (item.id == mediaId) {
                    item.copy(isFavorite = !item.isFavorite)
                } else {
                    item
                }
            }

            _uiState.value = _uiState.value.copy(
                searchResults = Resource.Success(updatedResults)
            )
        }
    }

    fun showMediaOptions(mediaId: String) {
        _uiState.value = _uiState.value.copy(
            selectedMediaId = mediaId,
            showMediaOptions = true
        )
    }

    fun hideMediaOptions() {
        _uiState.value = _uiState.value.copy(
            selectedMediaId = null,
            showMediaOptions = false
        )
    }

    private suspend fun getSearchHistoryFromStorage(): List<String> {
        // Implementation to get from DataStore or Room
        return emptyList()
    }

    private suspend fun saveSearchHistoryToStorage(history: List<String>) {
        // Implementation to save to DataStore or Room
    }
}

data class SearchUiState(
    val searchQuery: String = "",
    val searchResults: Resource<List<MediaItem>> = Resource.Success(emptyList()),
    val searchHistory: List<String> = emptyList(),
    val selectedFilter: MediaType? = null,
    val selectedMediaId: String? = null,
    val showMediaOptions: Boolean = false,
    val isLoading: Boolean = false
)
