package com.example.musicplayerlite.media

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicplayerlite.model.Song

class MusicManager(context: Context) : Player.Listener {

    val player =
        ExoPlayer.Builder(context).setWakeMode(C.WAKE_MODE_LOCAL).setAudioAttributes(
            AudioAttributes.Builder().setContentType(
                C.AUDIO_CONTENT_TYPE_MUSIC
            ).setUsage(C.USAGE_MEDIA)
                .build(), true
        ).build()

    init {
        player.addListener(this)
    }

    private var musicPlayerListener: IMusicPlayerListener? = null

    fun addListener(listener: IMusicPlayerListener) {
        musicPlayerListener = listener
    }

    fun reset() {
        player.stop()
        player.clearMediaItems()
    }
    /*fun play(mediaItem: MediaItem){
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }
    fun play(mediaItems: List<MediaItem>){
        player.setMediaItems(mediaItems, true)
        player.prepare()
        player.play()
    }*/

    fun play(audio: Song) {
        val mediaItem = MediaItem.fromUri(audio.mediaUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun play(audio: List<Song>) {
        val mediaItems = audio.map {
            MediaItem.fromUri(it.mediaUri)
        }
        player.setMediaItems(mediaItems, true)
        player.prepare()
    }

    fun next() {
        player.seekToNext()
    }

    fun previous() {
        player.seekToPrevious()
    }

    fun resume() {
        player.play()
    }


    fun pause() {
        player.pause()
    }

    fun release() {
        player.release()
    }


    override fun onPlaybackStateChanged(playbackState: Int) {
        if (playbackState == Player.STATE_READY) {
            musicPlayerListener?.onPrepared()
        } else if (playbackState == Player.STATE_ENDED) {
            musicPlayerListener?.onCompletion()
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        musicPlayerListener?.onError()
    }
}