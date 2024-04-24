package com.example.musicplayerlite.repository

import com.example.musicplayerlite.model.Album
import com.example.musicplayerlite.model.Artist
import com.example.musicplayerlite.model.Song
import kotlinx.coroutines.flow.Flow

interface IMediaRepository {
    val songs: Flow<List<Song>>
    val artists: Flow<List<Artist>>
    val albums: Flow<List<Album>>
}