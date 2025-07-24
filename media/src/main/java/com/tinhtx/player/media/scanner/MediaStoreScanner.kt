package com.tinhtx.player.media.scanner

import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore
import com.tinhtx.player.domain.model.MediaItem
import com.tinhtx.player.domain.model.MediaType
import com.tinhtx.player.media.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaStoreScanner @Inject constructor(
    private val context: Context,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend fun scanMediaFiles(): List<MediaItem> = withContext(dispatcher) {
        val mediaItems = mutableListOf<MediaItem>()
        val contentResolver: ContentResolver = context.contentResolver

        // Projection (các cột cần query - thêm đầy đủ cho metadata)
        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.MediaColumns.DURATION,
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.MediaColumns.DATE_MODIFIED,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.DATA // Để lấy path hoặc uri string
        )

        // Query cho audio
        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null, null, null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)) ?: ""
                val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE))
                val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)) ?: "" // Non-null default
                val album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)) ?: "" // Non-null default
                val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION))
                val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE))
                val dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED))
                val dateModified = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED))
                val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE))
                val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
                val uriString = path ?: "" // Hoặc build uri string: "content://media/external/audio/media/$id"

                mediaItems.add(
                    MediaItem(
                        id = id.toString(),
                        uri = uriString, // String: sử dụng path hoặc uri string
                        displayName = displayName,
                        title = title,
                        artist = artist,
                        album = album,
                        duration = duration,
                        size = size,
                        dateAdded = dateAdded,
                        dateModified = dateModified,
                        mimeType = mimeType,
                        albumArtUri = null, // TODO: Query album art
                        track = null,
                        year = null,
                        genre = null,
                        bitrate = null,
                        sampleRate = null,
                        type = MediaType.AUDIO,
                        path = path
                    )
                )
            }
        }

        // Query cho video (tương tự, artist và album set default "" dù không áp dụng)
        contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null, null, null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)) ?: ""
                val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE))
                val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION))
                val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE))
                val dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED))
                val dateModified = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED))
                val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE))
                val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
                val uriString = path ?: "" // Hoặc build uri string: "content://media/external/video/media/$id"

                mediaItems.add(
                    MediaItem(
                        id = id.toString(),
                        uri = uriString,
                        displayName = displayName,
                        title = title,
                        artist = "", // Default "" cho video
                        album = "", // Default "" cho video
                        duration = duration,
                        size = size,
                        dateAdded = dateAdded,
                        dateModified = dateModified,
                        mimeType = mimeType,
                        albumArtUri = null,
                        track = null,
                        year = null,
                        genre = null,
                        bitrate = null,
                        sampleRate = null,
                        type = MediaType.VIDEO,
                        path = path
                    )
                )
            }
        }

        mediaItems // Trả về list đã quét
    }

    // Hàm refresh một MediaItem cụ thể (dựa trên ID và type)
    suspend fun refreshMediaItem(itemId: Long, type: MediaType): MediaItem? = withContext(dispatcher) {
        val contentResolver: ContentResolver = context.contentResolver
        val uri = if (type == MediaType.AUDIO) MediaStore.Audio.Media.EXTERNAL_CONTENT_URI else MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.MediaColumns.DURATION,
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.MediaColumns.DATE_MODIFIED,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.DATA
        )

        contentResolver.query(
            uri,
            projection,
            "${MediaStore.MediaColumns._ID} = ?",
            arrayOf(itemId.toString()),
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)) ?: ""
                val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE))
                val artist = if (type == MediaType.AUDIO) (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)) ?: "") else ""
                val album = if (type == MediaType.AUDIO) (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)) ?: "") else ""
                val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION))
                val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE))
                val dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED))
                val dateModified = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED))
                val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE))
                val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
                val uriString = path ?: "" // Hoặc build uri string nếu cần

                return@withContext MediaItem(
                    id = id.toString(),
                    uri = uriString,
                    displayName = displayName,
                    title = title,
                    artist = artist,
                    album = album,
                    duration = duration,
                    size = size,
                    dateAdded = dateAdded,
                    dateModified = dateModified,
                    mimeType = mimeType,
                    albumArtUri = null, // TODO: Query album art
                    track = null,
                    year = null,
                    genre = null,
                    bitrate = null,
                    sampleRate = null,
                    type = type,
                    path = path
                )
            }
        }
        null // Nếu không tìm thấy
    }

    private val contentResolver: ContentResolver = context.contentResolver

    suspend fun scanAudioFiles(): List<MediaItem> = withContext(Dispatchers.IO) {
        val audioFiles = mutableListOf<MediaItem>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.MIME_TYPE
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} = 1"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn).toString()
                val title = cursor.getString(titleColumn) ?: "Unknown"
                val artist = cursor.getString(artistColumn) ?: "Unknown Artist"
                val album = cursor.getString(albumColumn) ?: "Unknown Album"
                val duration = cursor.getLong(durationColumn)
                val path = cursor.getString(dataColumn) ?: ""
                val size = cursor.getLong(sizeColumn)
                val dateAdded = cursor.getLong(dateAddedColumn) * 1000 // Convert to milliseconds
                val mimeType = cursor.getString(mimeTypeColumn) ?: ""

                val uri = "${MediaStore.Audio.Media.EXTERNAL_CONTENT_URI}/$id"

                audioFiles.add(
                    MediaItem(
                        id = id,
                        uri = uri,
                        displayName = title,
                        title = title,
                        artist = artist,
                        album = album,
                        duration = duration,
                        size = size,
                        dateAdded = dateAdded,
                        dateModified = dateAdded, // Use same as dateAdded for now
                        mimeType = mimeType,
                        albumArtUri = null, // Will be populated later if available
                        track = null, // Will be populated later if available
                        year = null, // Will be populated later if available
                        genre = null, // Will be populated later if available
                        bitrate = null, // Will be populated later if available
                        sampleRate = null, // Will be populated later if available
                        type = MediaType.AUDIO,
                        path = path
                    )
                )
            }
        }

        audioFiles
    }

    suspend fun scanVideoFiles(): List<MediaItem> = withContext(Dispatchers.IO) {
        val videoFiles = mutableListOf<MediaItem>()

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.ARTIST,
            MediaStore.Video.Media.ALBUM,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.MIME_TYPE
        )

        val sortOrder = "${MediaStore.Video.Media.TITLE} ASC"

        contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn).toString()
                val title = cursor.getString(titleColumn) ?: "Unknown"
                val artist = cursor.getString(artistColumn) ?: "Unknown"
                val album = cursor.getString(albumColumn) ?: "Unknown"
                val duration = cursor.getLong(durationColumn)
                val path = cursor.getString(dataColumn) ?: ""
                val size = cursor.getLong(sizeColumn)
                val dateAdded = cursor.getLong(dateAddedColumn) * 1000
                val mimeType = cursor.getString(mimeTypeColumn) ?: ""

                val uri = "${MediaStore.Video.Media.EXTERNAL_CONTENT_URI}/$id"

                videoFiles.add(
                    MediaItem(
                        id = id,
                        uri = uri,
                        displayName = title,
                        title = title,
                        artist = artist,
                        album = album,
                        duration = duration,
                        size = size,
                        dateAdded = dateAdded,
                        dateModified = dateAdded, // Use same as dateAdded for now
                        mimeType = mimeType,
                        albumArtUri = null, // Will be populated later if available
                        track = null, // Will be populated later if available
                        year = null, // Will be populated later if available
                        genre = null, // Will be populated later if available
                        bitrate = null, // Will be populated later if available
                        sampleRate = null, // Will be populated later if available
                        type = MediaType.VIDEO,
                        path = path
                    )
                )
            }
        }

        videoFiles
    }

    suspend fun scanAllMediaFiles(): List<MediaItem> {
        return scanAudioFiles() + scanVideoFiles()
    }
}
