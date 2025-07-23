package com.tinhtx.player.repository

import com.tinhtx.player.common.Resource
import com.tinhtx.player.local.database.dao.MediaDao
import com.tinhtx.player.local.database.entities.MediaEntity
import com.tinhtx.player.model.MediaItem
import com.tinhtx.player.model.MediaType
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.junit.runner.RunWith

@RunWith(MockitoJUnitRunner::class)
class MediaRepositoryImplTest {

    @Mock
    private lateinit var mediaDao: MediaDao

    private lateinit var repository: MediaRepositoryImpl

    @Test
    fun `getAllMediaItems should return success resource`() = runTest {
        // Given
        val entities = listOf(
            MediaEntity("1", "Song 1", "Artist 1", "Album 1", 180000, "uri1", mediaType = "AUDIO")
        )
        `when`(mediaDao.getAllMediaItems()).thenReturn(flowOf(entities))

        repository = MediaRepositoryImpl(mediaDao)

        // When
        val result = repository.getAllMediaItems().first()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(1, result.data?.size)
        assertEquals("Song 1", result.data?.first()?.title)
    }

    @Test
    fun `getAudioItems should return only audio items`() = runTest {
        // Given
        val entities = listOf(
            MediaEntity("1", "Song 1", "Artist 1", "Album 1", 180000, "uri1", mediaType = "AUDIO")
        )
        `when`(mediaDao.getAudioItems()).thenReturn(flowOf(entities))

        repository = MediaRepositoryImpl(mediaDao)

        // When
        val result = repository.getAudioItems().first()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(MediaType.AUDIO, result.data?.first()?.mediaType)
    }
}
