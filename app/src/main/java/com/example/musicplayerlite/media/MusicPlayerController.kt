package com.example.musicplayerlite.media

import android.content.Context
import com.example.musicplayerlite.model.Song

class MusicPlayerController(
    private val applicationContext: Context,
    private val musicPlayerManager: MusicPlayerManager,
) : IMusicPlayerController {

    init {
        musicPlayerManager.addListener(
            object : IMusicPlayerListener {
                override fun onError() {
                    /* no-op */
                }

                override fun onPrepared() {
                    /* no-op */
                }

                override fun onCompletion() {
                    nextSong()
                }
            }
        )
    }

    fun setListener(listener: IMusicPlayerListener) {
        musicPlayerManager.addListener(listener)
    }

    override fun playSongs(index: Int, songs: List<Song>) {
        musicPlayerManager.setMediaItems(songs)
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
        if (indexSong < 0) indexSong = musicPlayerManager.mediaItems.lastIndex
        musicPlayerManager.configMedia {
            reset()
            setDataSource(
                applicationContext,
                musicPlayerManager.mediaItems[indexSong].mediaUri
            )
            prepareAsync()
        }
    }

    var indexSong = 0
    override fun nextSong() {
        indexSong++
        if (indexSong > musicPlayerManager.mediaItems.lastIndex) indexSong =
            MediaConst.DEFAULT_INDEX
        musicPlayerManager.configMedia {
            reset()
            setDataSource(applicationContext, musicPlayerManager.mediaItems[indexSong].mediaUri)
            prepareAsync()
        }
    }

    override fun seekTo(duration: Int) = musicPlayerManager.configMedia { seekTo(duration) }

    override fun setLooping() = musicPlayerManager.configMedia { isLooping = true }

    override fun isPlaying(): Boolean = musicPlayerManager.getMediaPlayer().isPlaying

    fun setShuffle() = musicPlayerManager.configMedia { }
    fun getDuration(): Int = musicPlayerManager.getMediaPlayer().duration

}