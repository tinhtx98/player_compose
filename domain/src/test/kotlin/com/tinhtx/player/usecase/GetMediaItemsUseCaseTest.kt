package com.tinhtx.player.usecase

import com.tinhtx.player.common.Resource
import com.tinhtx.player.model.MediaItem
import com.tinhtx.player.model.MediaType
import com.tinhtx.player.repository.MediaRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.junit.runner.RunWith

@RunWith(MockitoJUnitRunner::class)
class GetMediaItemsUseCaseTest {

    @Mock
    private lateinit var mediaRepository: MediaRepository

    private lateinit var useCase: GetMediaItemsUseCase

    @Test
    fun `getAllMediaItems should return media items from repository`() = runTest {
        // Given
        val mediaItems = listOf(
            MediaItem(
                id = "1",
                title = "Test Song",
                artist = "Test Artist",
                album = "Test Album",
                duration = 180000,
                uri = "content://test",
                mediaType = MediaType.AUDIO
            )
        )
        val resource = Resource.Success(mediaItems)
        `when`(mediaRepository.getAllMediaItems()).thenReturn(flowOf(resource))

        useCase = GetMediaItemsUseCase(mediaRepository)

        // When & Then
        useCase.getAllMediaItems().collect { result ->
            assertTrue(result is Resource.Success)
            assertEquals(mediaItems, result.data)
        }
    }
}
