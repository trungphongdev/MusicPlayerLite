package com.example.musicplayerlite.media

import com.example.musicplayerlite.model.Song

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

   // fun currentSong(): Song

}