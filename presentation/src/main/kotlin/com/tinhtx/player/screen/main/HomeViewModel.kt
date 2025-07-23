package com.tinhtx.player.screen.main

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
class HomeViewModel @Inject constructor(
    private val getMediaItemsUseCase: GetMediaItemsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadMediaItems() {
        viewModelScope.launch {
            getMediaItemsUseCase.getAllMediaItems().collect { resource ->
                _uiState.value = when (resource) {
                    is Resource.Loading -> HomeUiState.Loading
                    is Resource.Success -> {
                        val mediaItems = resource.data ?: emptyList()
                        HomeUiState.Success(mediaItems)
                    }
                    is Resource.Error -> HomeUiState.Error(
                        resource.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    fun refreshMediaItems() {
        viewModelScope.launch {
            getMediaItemsUseCase.refreshMediaLibrary()
            loadMediaItems()
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val mediaItems: List<MediaItem>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
