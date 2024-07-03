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
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.musicplayerlite.broardcast.BecomingNoisyReceiver
import com.example.musicplayerlite.common.PlaybackInfoListener
import com.example.musicplayerlite.databinding.ActivityMainBinding
import com.example.musicplayerlite.extention.displayCutout
import com.example.musicplayerlite.extention.hideSystemBar
import com.example.musicplayerlite.extention.insetPaddingSystemBar
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
                viewModel.getMusicController().pauseSong()
            }
        }
    }


    private fun initViewsListener() {
        onBackPressedDispatcher.addCallback {
            val isEmptyBackStack = findNavController(R.id.nav_host_fragment).popBackStack().not()
            if (isEmptyBackStack) {
                finish()
            }
        }
        listenerPlayback = object : PlaybackInfoListener {
            override fun onDurationChanged(duration: Int) {
               viewModel.getMusicController().seekTo(duration)
            }

            override fun onPositionChanged(position: Int) {
                if (viewModel.getMusicController().currentIndex.value != position) {
                    viewModel.setMediaItems(position)
                    startForegroundService()
                }
            }

            override fun onStateChanged(action: Int) {
                when (action) {
                    PlaybackInfoListener.Action.PAUSED -> viewModel.getMusicController().pauseSong()
                    PlaybackInfoListener.Action.SHUFFLE -> viewModel.getMusicController().shuffleSong()
                    PlaybackInfoListener.Action.LOOPING -> viewModel.getMusicController().setLooping()
                    PlaybackInfoListener.Action.PREVIOUS -> viewModel.getMusicController().previousSong()
                    PlaybackInfoListener.Action.NEXT -> viewModel.getMusicController().nextSong()
                    PlaybackInfoListener.Action.PLAYING -> viewModel.getMusicController().pauseSong()
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
                    //setListSong(viewModel.songUiState.value)
                 //   viewModel.setMediaPlayer(getMediaPlayer())
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
