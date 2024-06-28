package com.example.musicplayerlite.media

import androidx.media3.common.Player
import com.example.musicplayerlite.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class MusicPlayerController(
    private val musicPlayerManager: MusicManager,
    private val coroutineScope: CoroutineScope,
) : IMusicPlayerController, KoinComponent {

    private val mediaItems: MutableList<Song> = mutableListOf()
    private var currentIndex = 0

    private fun setMediaItems(songs: List<Song>) {
        coroutineScope.launch {
            if (mediaItems != songs) {
                mediaItems.clear()
                mediaItems.addAll(songs)
            }
        }
    }

    override fun setListener(listener: IMusicPlayerListener) {
        musicPlayerManager.addListener(listener)
    }

    override fun currentSong(): Song {
       return mediaItems[currentIndex]
    }

    override fun release() {
        musicPlayerManager.release()
    }

    override fun init() {
        playSongs(currentIndex, mediaItems)
    }

    override fun playSongs(index: Int, songs: List<Song>) {
        setMediaItems(songs)
        songs.getOrNull(index)?.let(::playSong)
    }

    override fun playSong(song: Song) {
        musicPlayerManager.play(song)
    }

    override fun pauseSong() = musicPlayerManager.pause()
    override fun stopSong() = musicPlayerManager.reset()
    override fun previousSong() {
        musicPlayerManager.previous()
    }

    var indexSong = 0
    override fun nextSong() {
        musicPlayerManager.next()
    }

    override fun seekTo(duration: Int) = musicPlayerManager.player.seekTo(duration.toLong())

    override fun setLooping() {
        musicPlayerManager.player.repeatMode = Player.REPEAT_MODE_ALL
    }

    override fun isPlaying(): Boolean = musicPlayerManager.player.isPlaying


    fun getDuration(): Long = musicPlayerManager.player.duration

}