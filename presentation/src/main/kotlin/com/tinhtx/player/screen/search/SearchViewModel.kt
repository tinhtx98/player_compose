package com.tinhtx.player.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinhtx.player.common.Resource
import com.tinhtx.player.model.MediaItem
import com.tinhtx.player.usecase.GetMediaItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getMediaItemsUseCase: GetMediaItemsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isNotEmpty()) {
            search(query)
        } else {
            _uiState.value = SearchUiState.Idle
        }
    }

    fun search(query: String) {
        if (query.isBlank()) {
            _uiState.value = SearchUiState.Idle
            return
        }

        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading

            getMediaItemsUseCase.searchMediaItems(query).collect { resource ->
                _uiState.value = when (resource) {
                    is Resource.Loading -> SearchUiState.Loading
                    is Resource.Success -> {
                        val results = resource.data ?: emptyList()
                        SearchUiState.Success(results)
                    }
                    is Resource.Error -> SearchUiState.Error(
                        resource.message ?: "Search failed"
                    )
                }
            }
        }
    }
}

sealed class SearchUiState {
    object Idle : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val results: List<MediaItem>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}
