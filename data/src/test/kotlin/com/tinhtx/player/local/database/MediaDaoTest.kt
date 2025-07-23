package com.tinhtx.player.local.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tinhtx.player.local.database.dao.MediaDao
import com.tinhtx.player.local.database.entities.MediaEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MediaDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var mediaDao: MediaDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        mediaDao = database.mediaDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetMediaItem() = runTest {
        val mediaItem = MediaEntity(
            id = "1",
            title = "Test Song",
            artist = "Test Artist",
            album = "Test Album",
            duration = 180000,
            uri = "content://test",
            mediaType = "AUDIO"
        )

        mediaDao.insertMediaItem(mediaItem)
        val result = mediaDao.getMediaItemById("1").first()

        assertEquals(mediaItem, result)
    }

    @Test
    fun getAllMediaItems() = runTest {
        val mediaItems = listOf(
            MediaEntity("1", "Song 1", "Artist 1", "Album 1", 180000, "uri1", mediaType = "AUDIO"),
            MediaEntity("2", "Song 2", "Artist 2", "Album 2", 200000, "uri2", mediaType = "AUDIO")
        )

        mediaDao.insertMediaItems(mediaItems)
        val result = mediaDao.getAllMediaItems().first()

        assertEquals(2, result.size)
    }

    @Test
    fun searchMediaItems() = runTest {
        val mediaItem = MediaEntity(
            id = "1",
            title = "Test Song",
            artist = "Test Artist",
            album = "Test Album",
            duration = 180000,
            uri = "content://test",
            mediaType = "AUDIO"
        )

        mediaDao.insertMediaItem(mediaItem)
        val result = mediaDao.searchMediaItems("Test").first()

        assertEquals(1, result.size)
        assertEquals(mediaItem, result.first())
    }
}
