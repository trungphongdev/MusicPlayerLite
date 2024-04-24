package com.example.musicplayerlite.service

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.musicplayerlite.common.Const
import com.example.musicplayerlite.common.NOTIFY_ID
import com.example.musicplayerlite.common.createNotification
import com.example.musicplayerlite.common.createNotificationChannel
import com.example.musicplayerlite.datastore.IMusicDataStore
import com.example.musicplayerlite.datastore.dataStore
import com.example.musicplayerlite.model.Song
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.random.Random


class MusicService : LifecycleService(), MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {
    private var mediaPlayer: MediaPlayer? = null
    private val binder = MusicBinder()
    private var indexSong: Int = Const.NO_POSITION
    private var listSong: List<Song> = emptyList()
    private var shuffle: Boolean = false
    private val musicDataStore: IMusicDataStore by inject()

    override fun onCreate() {
        super.onCreate()
        initMediaPlayer()
        createNotificationChannel(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        playSong()
        return START_NOT_STICKY
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mp?.start()
        lifecycleScope.launch {
            if (isActive) {
                musicDataStore.updateCurrentSong(
                    indexSong = indexSong,
                    song = listSong[indexSong],
                    isPlaying = true,
                    duration = getDuration(),
                )
            }
        }
        startForegroundService()
    }


    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        mediaPlayer?.reset()
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        stopSelf()
    }


    override fun onCompletion(mp: MediaPlayer?) {
        if (mp != null) {
            mp.reset()
            playNextSong()
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return false
    }

    private fun startForegroundService() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            if (notificationPermission == PackageManager.PERMISSION_DENIED) {
                return
            }
        }

        ServiceCompat.startForeground(
            /* service = */ this,
            /* id = */ NOTIFY_ID,
            /* notification = */ createNotification(applicationContext, listSong.get(indexSong)),
            /* foregroundServiceType = */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            } else {
                0
            },
        )
    }

    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer().apply {
            setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setOnPreparedListener(this@MusicService)
            setOnErrorListener(this@MusicService)
            setOnCompletionListener(this@MusicService)
        }
    }

    fun playNextSong() {
        if (shuffle) {
            indexSong = Random.nextInt(listSong.size)
        } else {
            indexSong++
            if (indexSong == listSong.count() - 1) indexSong = 0
        }
        playSong()
    }

    fun setShuffle() { shuffle = shuffle.not() }
    fun setLooping() { mediaPlayer?.isLooping = true}
    fun seekTo(duration: Int) = mediaPlayer?.seekTo(duration)
    private fun getDuration(): Int = mediaPlayer?.duration ?: 0
    fun getPosition(): Int? = mediaPlayer?.currentPosition
    fun getIndexSong(): Int = indexSong
    fun hasPlaying(): Boolean = mediaPlayer?.isPlaying ?: false
    fun pauseSong() {
        mediaPlayer?.pause()
        lifecycleScope.launch {
            if (isActive) {
                musicDataStore.updateCurrentSong(
                    indexSong = indexSong,
                    song = listSong[indexSong],
                    isPlaying = false,
                    duration = getDuration(),
                )
            }
        }
    }
    fun resumeSong() {
        mediaPlayer?.start()
        lifecycleScope.launch {
            if (isActive) {
                musicDataStore.updateCurrentSong(
                    indexSong = indexSong,
                    song = listSong[indexSong],
                    isPlaying = true,
                    duration = getDuration(),
                )
            }
        }
    }
    fun getMediaPlayer(): MediaPlayer? = mediaPlayer

    fun setSongIndex(index: Int) {
        if (index == indexSong) return
        indexSong = index
    }

    fun setListSong(songs: List<Song>) {
        if (listSong.count() != songs.count()) {
            listSong = songs
        }
    }

    private fun playSong() {
        val currentSong = listSong.getOrNull(indexSong) ?: return
        mediaPlayer?.reset()
        mediaPlayer?.setDataSource(applicationContext, currentSong.mediaUri)
        mediaPlayer?.prepareAsync()
    }

    fun playPrevious() {
        indexSong--
        if (indexSong < 0) indexSong = listSong.size - 1
        playSong()
    }
}



