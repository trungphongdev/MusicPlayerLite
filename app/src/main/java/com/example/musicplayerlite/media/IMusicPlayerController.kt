package com.example.musicplayerlite.media

import com.example.musicplayerlite.model.Song
import kotlinx.coroutines.flow.MutableStateFlow

interface IMusicPlayerController {
    fun playSongs(index: Int, songs: List<Song>)
    fun playSong(song: Song)
    fun pauseSong();
    fun stopSong();
    fun previousSong()
    fun nextSong()
    fun setLooping()
    fun isPlaying(): Boolean
    fun seekTo(duration: Int)
    fun setListener(listener: IMusicPlayerListener)
    fun currentSong(): Song
    fun release()
    fun init()

    fun getCurrentPositionPlaying(): Int

    fun setMediaItems(songs: List<Song>)
    fun shuffleSong()

    val currentIndex: MutableStateFlow<Int>
}