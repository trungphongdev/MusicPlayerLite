package com.example.musicplayerlite.media

import android.content.Context
import com.example.musicplayerlite.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class MusicPlayerController(
    private val applicationContext: Context,
    private val musicPlayerManager: MusicPlayerManager,
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
        musicPlayerManager.configMedia {
            reset()
            release()
        }
    }

    override fun init() {
        playSongs(currentIndex, mediaItems)
    }

    override fun playSongs(index: Int, songs: List<Song>) {
        setMediaItems(songs)
        songs.getOrNull(index)?.let(::playSong)
    }

    override fun playSong(song: Song) {
        musicPlayerManager.configMedia {
            reset()
            setDataSource(applicationContext, song.mediaUri)
            prepareAsync()
        }
    }

    override fun pauseSong() = musicPlayerManager.configMedia { pause() }
    override fun stopSong() = musicPlayerManager.configMedia { stop() }
    override fun previousSong() {
        indexSong--
        if (indexSong < 0) indexSong = mediaItems.lastIndex
        musicPlayerManager.configMedia {
            reset()
            setDataSource(
                applicationContext,
                mediaItems[indexSong].mediaUri
            )
            prepareAsync()
        }
    }

    var indexSong = 0
    override fun nextSong() {
        indexSong++
        if (indexSong > mediaItems.lastIndex) indexSong =
            MediaConst.DEFAULT_INDEX
        musicPlayerManager.configMedia {
            reset()
            setDataSource(applicationContext, mediaItems[indexSong].mediaUri)
            prepareAsync()
        }
    }

    override fun seekTo(duration: Int) = musicPlayerManager.configMedia { seekTo(duration) }

    override fun setLooping() = musicPlayerManager.configMedia { isLooping = true }

    override fun isPlaying(): Boolean = musicPlayerManager.getMediaPlayer().isPlaying

    fun setShuffle() = musicPlayerManager.configMedia { }
    fun getDuration(): Int = musicPlayerManager.getMediaPlayer().duration

}