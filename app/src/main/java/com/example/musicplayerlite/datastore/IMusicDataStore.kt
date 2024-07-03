package com.example.musicplayerlite.datastore

import com.example.musicplayerlite.model.MusicState
import com.example.musicplayerlite.model.Song
import kotlinx.coroutines.flow.Flow

typealias SongId = String

interface IMusicDataStore {

    val musicCurrentState: Flow<MusicState?>
    val favouriteSongIds: Flow<MutableSet<SongId>>

    suspend fun updateCurrentSong(
        indexSong: Int,
        song: Song,
        isPlaying: Boolean,
    )

    suspend fun setFavouriteSong(id: SongId)
}