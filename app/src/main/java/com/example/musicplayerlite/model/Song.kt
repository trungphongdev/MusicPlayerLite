package com.example.musicplayerlite.model

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import com.example.musicplayerlite.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val id: String,
    val artistId: Long,
    val albumId: Long,
    val mediaUri: Uri,
    val artworkUri: Uri,
    val title: String,
    val artist: String,
    val album: String,
    val folder: String,
    val duration: Long,
    val date: Long,
    val isFavorite: Boolean,
): Parcelable {
    fun getNameArtist(context: Context): String {
        return artist
            .takeUnless { it == context.getString(R.string.unknown) }
            ?: context.getString(R.string.unknown_artist)
    }
}

