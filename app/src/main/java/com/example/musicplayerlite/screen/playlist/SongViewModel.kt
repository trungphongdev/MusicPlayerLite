package com.example.musicplayerlite.screen.playlist

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayerlite.common.Const
import com.example.musicplayerlite.datastore.IMusicDataStore
import com.example.musicplayerlite.datastore.SongId
import com.example.musicplayerlite.media.IMusicPlayerController
import com.example.musicplayerlite.media.MusicPlayerController
import com.example.musicplayerlite.model.Song
import com.example.musicplayerlite.repository.IMediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SongViewModel(
    private val savedStateHandle: SavedStateHandle,
    mediaRepository: IMediaRepository,
    private val musicDataStore: IMusicDataStore,
    private val musicPlayerController: IMusicPlayerController,
) : ViewModel() {

    private
    var mediaPlayer: MediaPlayer? = null
        private set
    private val _serviceConnected = MutableStateFlow(false)
    val serviceConnected = _serviceConnected.asStateFlow()
    val songUiState = mediaRepository.songs.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val musicStateFlow = musicDataStore.musicCurrentState.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val albumsStateFlow = mediaRepository.albums.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val artistStateFlow = mediaRepository.artists.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val favouriteSongsId = musicDataStore.favouriteSongIds.stateIn(viewModelScope, SharingStarted.Eagerly, emptySet<SongId>())

    fun setServiceHasConnected(isConnect: Boolean) {
        _serviceConnected.value = isConnect
    }


    fun setMediaPlayer(mediaPlayer: MediaPlayer?) {
        this.mediaPlayer = mediaPlayer
    }

    fun updateFavouriteSong(id: SongId) {
        viewModelScope.launch {
            musicDataStore.setFavouriteSong(id)
        }
    }
}