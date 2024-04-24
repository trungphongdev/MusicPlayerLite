package com.example.musicplayerlite.datastore

import com.example.musicplayerlite.model.MusicState
import com.example.musicplayerlite.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IMusicDataStore {

    val musicCurrentState: Flow<MusicState?>

   suspend fun updateCurrentSong(
        indexSong: Int,
        song: Song,
        isPlaying: Boolean,
        duration: Int,
    )

}