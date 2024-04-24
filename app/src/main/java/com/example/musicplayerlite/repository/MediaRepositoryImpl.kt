package com.example.musicplayerlite.repository

import android.net.Uri
import com.example.musicplayerlite.datasource.MusicMediaDataSource
import com.example.musicplayerlite.datastore.IMusicDataStore
import com.example.musicplayerlite.datastore.MusicDataStore
import com.example.musicplayerlite.model.Album
import com.example.musicplayerlite.model.Artist
import com.example.musicplayerlite.model.Song
import com.example.musicplayerlite.model.SortBy
import com.example.musicplayerlite.model.SortOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MediaRepositoryImpl(
    musicMediaDataSource: MusicMediaDataSource,
    //@AppDispatcherIO dispatcher: CoroutineDispatcher
) : IMediaRepository {
    override val songs: Flow<List<Song>> = musicMediaDataSource.getSongs(
        sortBy = SortBy.DATE,
        sortOrder = SortOrder.ASCENDING,
    )
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)


    override val artists: Flow<List<Artist>> = songs.map(transform = { songs ->
        songs.groupBy(Song::artistId).map { (artistId, songs) ->
            Artist(id = artistId, songs = songs, name = songs.first().artist)
        }
    })
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)


    override val albums: Flow<List<Album>> = songs.map(transform = { songs ->
        songs.groupBy(Song::albumId).map { (albumId, songs) ->
            val song = songs.first()
            Album(
                id = albumId,
                name = song.album,
                artworkUri = song.artworkUri,
                artist = song.artist,
                songs = songs,
            )
        }
    })
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)

}