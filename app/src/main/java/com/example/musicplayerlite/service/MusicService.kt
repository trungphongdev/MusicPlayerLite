package com.example.musicplayerlite.service

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.musicplayerlite.common.NOTIFY_ID
import com.example.musicplayerlite.common.createNotification
import com.example.musicplayerlite.common.createNotificationChannel
import com.example.musicplayerlite.datastore.IMusicDataStore
import com.example.musicplayerlite.media.IMusicPlayerController
import com.example.musicplayerlite.media.IMusicPlayerListener
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class MusicService : LifecycleService()  {
    private val binder = MusicBinder()
    private val musicController: IMusicPlayerController by inject()
    private val musicDataStore: IMusicDataStore by inject()

    private val mediaListener = object: IMusicPlayerListener {
        override fun onError() {
            /* no-op */
        }

        override fun onPrepared() {
            lifecycleScope.launch {
                musicDataStore.updateCurrentSong(
                    indexSong = musicController.currentIndex.value,
                    song = musicController.currentSong(),
                    isPlaying = true,
                )
            }
            startForegroundService()
        }

        override fun onCompletion() {
            musicController.nextSong()
        }
    }


    override fun onCreate() {
        super.onCreate()
        musicController.setListener(mediaListener)
        createNotificationChannel(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
         musicController.init()
        return START_NOT_STICKY
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        musicController.release()
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
            this,
            NOTIFY_ID,
            createNotification(applicationContext, musicController.currentSong()),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            } else {
                0
            },
        )
    }
}



