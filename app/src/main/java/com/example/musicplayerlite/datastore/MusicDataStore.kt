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
import kotlinx.coroutines.flow.firstOrNull
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
            pref[CURRENT_SONG]?.let { Json.decodeFromString<MusicState>(it) }
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
    ): Unit = withContext(Dispatchers.IO) {
        dataStore.edit { pref ->
            val songJson = Json.encodeToString(MusicState(indexSong, song, isPlaying))
            pref[CURRENT_SONG] = songJson
        }
    }

    override val favouriteSongIds: Flow<MutableSet<SongId>> = dataStore.data.map {
        val setSongsId = it[FAVOURITE_SONGS] ?: emptySet()
        setSongsId.toMutableSet()
    }.distinctUntilChanged()
        .flowOn(Dispatchers.IO)

    override suspend fun setFavouriteSong(id: SongId): Unit = withContext(Dispatchers.IO) {
        dataStore.edit { pref ->
            val favouriteSetSongsId = favouriteSongIds.firstOrNull()?.toMutableSet()
            val isNotFavourite = favouriteSetSongsId?.none { it == id } ?: false
            if (isNotFavourite) {
                favouriteSetSongsId?.add(id)
            } else {
                favouriteSetSongsId?.remove(id)
            }
            pref[FAVOURITE_SONGS] = favouriteSetSongsId.orEmpty()
        }
    }


}