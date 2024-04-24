package com.example.musicplayerlite.model

import android.net.Uri

data class Album(
    val id: Long,
    val name: String,
    val artworkUri: Uri,
    val artist: String,
    val songs: List<Song>,
)