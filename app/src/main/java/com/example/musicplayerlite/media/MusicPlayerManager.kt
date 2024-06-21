package com.example.musicplayerlite.media

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.PowerManager
import com.example.musicplayerlite.model.Song

class MusicPlayerManager(private val applicationContext: Context) : MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private val mediaPlayer = MediaPlayer()

    private var IMusicPlayerListener: IMusicPlayerListener? = null

    fun addListener(listener: IMusicPlayerListener) {
        IMusicPlayerListener = listener
    }

    val mediaItems: MutableList<Song> = mutableListOf()


    fun setMediaItems(songs: List<Song>) {
        if (mediaItems != songs) {
            mediaItems.clear()
            mediaItems.addAll(songs)
        }
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
        IMusicPlayerListener?.onPrepared()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        mediaPlayer.reset()
        IMusicPlayerListener?.onError()
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mediaPlayer.reset()
        IMusicPlayerListener?.onCompletion()
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

