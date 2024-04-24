package com.example.musicplayerlite.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.musicplayerlite.model.MusicState
import com.example.musicplayerlite.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "music_player")

class MusicDataStore(
    private val dataStore: DataStore<Preferences>
) : IMusicDataStore {

    override val musicCurrentState: Flow<MusicState?> = dataStore.data.map { pref ->
        try {
            pref[CURRENT_MUSIC]?.let { Json.decodeFromString<MusicState>(it) }.also {
            }
        } catch (e: Exception) {
            null
        }
    }
        .filterNot { it == null }
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)

    override suspend fun updateCurrentSong(
        indexSong: Int,
        song: Song,
        isPlaying: Boolean,
        duration: Int
    ): Unit = withContext(Dispatchers.IO) {
        dataStore.edit { pref ->
            val songJson = Json.encodeToString(MusicState(indexSong, song, isPlaying, duration))
            pref[CURRENT_MUSIC] = songJson
        }
    }


}