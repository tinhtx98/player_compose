package com.tinhtx.player.usecase

import com.tinhtx.player.common.Resource
import com.tinhtx.player.model.MediaItem
import com.tinhtx.player.model.SortOrder
import com.tinhtx.player.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMediaItemsUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {

    fun getAllMediaItems(): Flow<Resource<List<MediaItem>>> {
        return mediaRepository.getAllMediaItems()
    }

    fun getAudioItems(): Flow<Resource<List<MediaItem>>> {
        return mediaRepository.getAudioItems()
    }

    fun getVideoItems(): Flow<Resource<List<MediaItem>>> {
        return mediaRepository.getVideoItems()
    }

    fun getMediaItemById(id: String): Flow<Resource<MediaItem?>> {
        return mediaRepository.getMediaItemById(id)
    }

    fun searchMediaItems(query: String): Flow<Resource<List<MediaItem>>> {
        return mediaRepository.searchMediaItems(query)
    }

    suspend fun refreshMediaLibrary(): Resource<Unit> {
        return mediaRepository.refreshMediaLibrary()
    }
}
