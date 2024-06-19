package com.example.musicplayerlite.screen.play

import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.musicplayerlite.App
import com.example.musicplayerlite.MainActivity
import com.example.musicplayerlite.R
import com.example.musicplayerlite.base.BaseFragment
import com.example.musicplayerlite.common.Const
import com.example.musicplayerlite.common.PlaybackInfoListener
import com.example.musicplayerlite.databinding.FragmentPlaySongBinding
import com.example.musicplayerlite.extention.toWholeSeconds
import com.example.musicplayerlite.screen.playlist.SongViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class PlaySongFragment : BaseFragment<FragmentPlaySongBinding>() {
    private val viewModel by activityViewModel<SongViewModel>()
    private var jobCurrentPosition: Job? = null
    private val durationTotal
        get() = viewModel.mediaPlayer?.duration?.toLong().toWholeSeconds()
    private val durationCurrent
        get() = viewModel.mediaPlayer?.currentPosition?.toLong().toWholeSeconds()

    override fun getViewBinding(): FragmentPlaySongBinding {
        return FragmentPlaySongBinding.inflate(layoutInflater)
    }

    override fun initData() {

    }

    override fun initView() {
        viewModel.musicStateFlow
            .filterNot { it == null }
            .onEach { uiState ->
                val song = uiState?.song!!
                with(binding) {
                    tvNameSong.text = song.title
                    tvArtist.text = song.getNameArtist(baseContext)
                    binding.seekbarSong.max = 0
                    binding.seekbarSong.progress = 0
                    setUpSeekbarAndTimeSong()
                    Glide.with(this@PlaySongFragment)
                        .load(song.artworkUri)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .placeholder(R.drawable.ic_song_placeholder)
                        .error(R.drawable.ic_song_placeholder)
                        .into(imvAlbumArtist)
                    120.seconds.toComponents { minutes, seconds, nanoseconds ->
                        binding.tvMaxTimeSong.text = "$minutes   $seconds"
                        Log.d("tag123", "toDuration: " + minutes)
                    }
                    song.duration.toDuration(DurationUnit.MINUTES).inWholeMinutes.also {
                        Log.d("tag123", "initView: " + it)
                    }
                }
            }.launchIn(lifecycleScope)

    }


    private fun setUpSeekbarAndTimeSong() {
        jobCurrentPosition?.cancel()
        binding.seekbarSong.max = durationTotal
        binding.seekbarSong.progress = durationCurrent
        jobCurrentPosition = (baseContext.applicationContext as App).applicationScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                val durationRemaining = durationTotal - durationCurrent
                repeat(durationRemaining) {
                    binding.seekbarSong.progress = durationCurrent + Const.ONE_SECOND_INTERVAL
                    delay(1.seconds)
                }
            }
        }
    }

    override fun initViewListener() {
        binding.imvBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.seekbarSong.setOnStopTrackingTouch { progress ->
            (activity as MainActivity).listenerPlayback.onDurationChanged(progress)
        }
        binding.imvShuffle.setOnClickListener {
            (activity as MainActivity).listenerPlayback.onStateChanged(PlaybackInfoListener.Action.SHUFFLE)
        }

        binding.imvPrevious.setOnClickListener {
            (activity as MainActivity).listenerPlayback.onStateChanged(PlaybackInfoListener.Action.PREVIOUS)
        }

        binding.imvNext.setOnClickListener {
            (activity as MainActivity).listenerPlayback.onStateChanged(PlaybackInfoListener.Action.NEXT)
        }
        binding.imvPause.setOnClickListener {
            (activity as MainActivity).listenerPlayback.onStateChanged(PlaybackInfoListener.Action.PAUSED)
        }
        binding.imvLooping.setOnClickListener {
            (activity as MainActivity).listenerPlayback.onStateChanged(PlaybackInfoListener.Action.LOOPING)
        }
        binding.imvFavourite.setOnClickListener {
            val songId = viewModel.musicStateFlow.value?.song?.id ?: return@setOnClickListener
            viewModel.updateFavouriteSong(songId)
        }

    }

    private fun AppCompatSeekBar.setOnStopTrackingTouch(
        onProgressChanged: (Int) -> Unit
    ) {
        this.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    /* no-op */
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    /* no-op */
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    onProgressChanged(seekBar?.progress ?: 0)
                }
            }
        )
    }

    override fun initObserver() {
        viewModel.favouriteSongsId.onEach { songsId ->
            val songId = viewModel.musicStateFlow.value?.song?.id
            val isFavourite = songsId.contains(songId)
            binding.imvFavourite.isSelected = isFavourite
        }.launchIn(lifecycleScope)
    }
}