package com.tinhtx.player.presentation.screen.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinhtx.player.common.Resource
import com.tinhtx.player.model.MediaItem
import com.tinhtx.player.domain.usecase.GetMediaItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val getMediaItemsUseCase: GetMediaItemsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CollectionUiState>(CollectionUiState.Loading)
    val uiState: StateFlow<CollectionUiState> = _uiState.asStateFlow()

    init {
        loadCollection()
    }

    private fun loadCollection() {
        viewModelScope.launch {
            getMediaItemsUseCase.getAllMediaItems().collect { resource ->
                _uiState.value = when (resource) {
                    is Resource.Loading -> CollectionUiState.Loading
                    is Resource.Success -> {
                        val mediaItems = resource.data ?: emptyList()
                        CollectionUiState.Success(mediaItems)
                    }
                    is Resource.Error -> CollectionUiState.Error(
                        resource.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    fun refreshCollection() {
        viewModelScope.launch {
            getMediaItemsUseCase.refreshMediaLibrary()
            loadCollection()
        }
    }
}

sealed class CollectionUiState {
    object Loading : CollectionUiState()
    data class Success(val mediaItems: List<MediaItem>) : CollectionUiState()
    data class Error(val message: String) : CollectionUiState()
}
