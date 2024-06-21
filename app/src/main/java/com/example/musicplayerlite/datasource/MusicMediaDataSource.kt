package com.example.musicplayerlite.datasource

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.os.Build
import android.provider.MediaStore
import com.example.musicplayerlite.extentions.asArtworkUri
import com.example.musicplayerlite.extentions.asFolder
import com.example.musicplayerlite.model.Song
import com.example.musicplayerlite.model.SortBy
import com.example.musicplayerlite.model.SortOrder
import com.example.musicplayerlite.utils.observer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MusicMediaDataSource(
    private val contentResolver: ContentResolver
) {

    @SuppressLint("BuildListAdds")
    fun getSongs(
        sortOrder: SortOrder,
        sortBy: SortBy,
    ): Flow<List<Song>> = contentResolver.observer(collection).map {
        buildList {
            contentResolver.query(
                collection,
                projection,
                null,
                null,
                buildMediaStoreSortOrder(sortBy, sortOrder)
            )?.use { cursor ->
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    val albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                    val artistId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID))
                    val album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    val date = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED))
                    val folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)).asFolder()
                    val mediaUri = ContentUris.withAppendedId(collection, id)
                    val artworkUri = albumId.asArtworkUri()
                    Song(
                        id = id.toString(),
                        artistId = artistId,
                        albumId = albumId,
                        mediaUri = mediaUri,
                        title = title,
                        artworkUri = artworkUri,
                        artist = artist,
                        album = album,
                        folder = folder,
                        duration = duration,
                        date = date,
                        isFavorite = false
                    ).let(::add)
                }
                cursor.close()
            }
        }
    }

    private val collection = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
        MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }
    private val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.ARTIST_ID,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.DATE_ADDED,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.DISPLAY_NAME
    )
    private val mediaSortByMap = mapOf(
        SortBy.ALBUM to MediaStore.Audio.Media.ALBUM,
        SortBy.TITLE to MediaStore.Audio.Media.TITLE,
        SortBy.ARTIST to MediaStore.Audio.Media.ARTIST,
        SortBy.DATE to MediaStore.Audio.Media.DATE_ADDED,
        SortBy.DURATION to MediaStore.Audio.Media.DURATION,
    )
    private val mediaSortOrderMap = mapOf(
        SortOrder.ASCENDING to "ASC",
        SortOrder.DESCENDING to "DESC"
    )

    private fun buildMediaStoreSortOrder(sortBy: SortBy, sortOrder: SortOrder): String {
        val mediaSortBy = mediaSortByMap[sortBy]
        val mediaSortOrder = mediaSortOrderMap[sortOrder]
        return "$mediaSortBy $mediaSortOrder"
    }
}