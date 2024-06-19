package com.example.musicplayerlite.media

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.PowerManager
import com.example.musicplayerlite.model.Song

class MusicPlayerManager(private val applicationContext: Context) : MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private val mediaPlayer = MediaPlayer()

    init {
        try {
            mediaPlayer.apply {
                setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setOnPreparedListener(this@MusicPlayerManager)
                setOnErrorListener(this@MusicPlayerManager)
                setOnCompletionListener(this@MusicPlayerManager)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer.start()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        mediaPlayer.reset()
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mediaPlayer.reset()
    }


    fun playSong(song: Song) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(applicationContext, song.mediaUri)
        mediaPlayer.prepareAsync()
    }

    fun configMedia(builder: MediaPlayer.() -> Unit) {
     mediaPlayer.builder()
    }

    fun getMediaPlayer(): MediaPlayer {
        return mediaPlayer
    }
}