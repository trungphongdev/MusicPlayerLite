package com.example.musicplayerlite

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.media.AudioManager
import android.os.Bundle
import android.os.IBinder
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.musicplayerlite.broardcast.BecomingNoisyReceiver
import com.example.musicplayerlite.common.PlaybackInfoListener
import com.example.musicplayerlite.databinding.ActivityMainBinding
import com.example.musicplayerlite.extention.displayCutout
import com.example.musicplayerlite.extention.hideSystemBar
import com.example.musicplayerlite.extention.setStatusBarColor
import com.example.musicplayerlite.screen.playlist.SongViewModel
import com.example.musicplayerlite.service.MusicService
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mBound: Boolean = false
    private var mService: MusicService? = null
    private val viewModel by viewModel<SongViewModel>()
    lateinit var listenerPlayback: PlaybackInfoListener
    private val intentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
    private val myNoisyAudioStreamReceiver = BecomingNoisyReceiver()

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MusicService.MusicBinder
            mService = binder.getService()
            mBound = true
            viewModel.setServiceHasConnected(true)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        hideSystemBar()
        setStatusBarColor(android.R.color.transparent)
        displayCutout()
        setContentView(binding.root)
        initObservers()
        initViewsListener()
        myNoisyAudioStreamReceiver.onBecomingNoisy = {
            if (it) {
                mService?.pauseSong()
            }
        }
    }


    private fun initViewsListener() {
        onBackPressedDispatcher.addCallback {
            if (findNavController(R.id.nav_host_fragment).popBackStack().not()) {
                finish()
            }
        }
        listenerPlayback = object : PlaybackInfoListener {
            override fun onDurationChanged(duration: Int) {
                mService?.seekTo(duration)
            }

            override fun onPositionChanged(position: Int) {
                if (position != mService?.getPosition()) {
                    mService?.setSongIndex(position)
                    startForegroundService()
                }
            }

            override fun onStateChanged(action: Int) {
                when (action) {
                    PlaybackInfoListener.Action.PAUSED -> mService?.pauseSong()
                    PlaybackInfoListener.Action.SHUFFLE -> mService?.setShuffle()
                    PlaybackInfoListener.Action.LOOPING -> mService?.setLooping()
                    PlaybackInfoListener.Action.PREVIOUS -> mService?.playPrevious()
                    PlaybackInfoListener.Action.NEXT -> mService?.playNextSong()
                    PlaybackInfoListener.Action.PLAYING -> mService?.resumeSong()
                }
            }

            override fun onPlayBackCompleted() {
                /* no-op */
            }

        }
    }

    private fun initObservers() {
        viewModel.serviceConnected.onEach { isConnected ->
            if (isConnected && mService != null) {
                mService?.apply {
                    setListSong(viewModel.songUiState.value)
                    viewModel.setMediaPlayer(getMediaPlayer())
                }
            }
        }.launchIn(lifecycleScope)
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(myNoisyAudioStreamReceiver, intentFilter)
        bindService(
            Intent(this@MainActivity, MusicService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onResume() {
        super.onResume()
        volumeControlStream = AudioManager.STREAM_MUSIC
    }


    override fun onStop() {
        super.onStop()
        unregisterReceiver(myNoisyAudioStreamReceiver)
        unbindService(connection)
        mBound = false
        mService = null
        viewModel.setServiceHasConnected(false)
    }


    private fun startForegroundService() {
        val intent = Intent(this@MainActivity, MusicService::class.java)
        ContextCompat.startForegroundService(this@MainActivity, intent)
    }

}
