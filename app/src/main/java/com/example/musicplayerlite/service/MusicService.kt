package com.example.musicplayerlite.service

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.musicplayerlite.common.Const
import com.example.musicplayerlite.common.NOTIFY_ID
import com.example.musicplayerlite.common.createNotification
import com.example.musicplayerlite.common.createNotificationChannel
import com.example.musicplayerlite.datastore.IMusicDataStore
import com.example.musicplayerlite.media.MusicPlayerController
import com.example.musicplayerlite.model.Song
import com.example.musicplayerlite.media.MusicPlayerManager
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.random.Random


class MusicService : LifecycleService()  {
    private val binder = MusicBinder()
    private var indexSong: Int = Const.NO_POSITION
    private val musicDataStore: IMusicDataStore by inject()
    private val musicController: MusicPlayerController by inject()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
       // listSong.getOrNull(indexSong)?.let(musicPlayerManager::playSong)
        return START_NOT_STICKY
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

/*    override fun onPrepared(mp: MediaPlayer?) {
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
    }*/

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
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
            /* notification = */ createNotification(applicationContext,/*listSong[indexSong]*/),
            /* foregroundServiceType = */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            } else {
                0
            },
        )
    }
}



