package com.example.musicplayerlite.media

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.PowerManager

class MusicPlayerManager(private val applicationContext: Context) : MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private val mediaPlayer = MediaPlayer()

    private var musicPlayerListener: IMusicPlayerListener? = null

    fun addListener(listener: IMusicPlayerListener) {
        musicPlayerListener = listener
    }

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
        musicPlayerListener?.onPrepared()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        mediaPlayer.reset()
        musicPlayerListener?.onError()
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mediaPlayer.reset()
        musicPlayerListener?.onCompletion()
    }

    fun configMedia(builder: MediaPlayer.() -> Unit) {
     mediaPlayer.builder()
    }

    fun getMediaPlayer(): MediaPlayer {
        return mediaPlayer
    }
}

interface IMusicPlayerListener {
    abstract fun onPrepared()
    abstract fun onError()
    abstract fun onCompletion()
}

