package com.example.musicplayerlite.screen.home

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.musicplayerlite.R
import com.example.musicplayerlite.adapter.FOLLOWED_ARTIST
import com.example.musicplayerlite.adapter.LIKE_SONG
import com.example.musicplayerlite.adapter.MusicActivitiesAdapter
import com.example.musicplayerlite.adapter.PLAY_LIST
import com.example.musicplayerlite.adapter.YOUR_HISTORY
import com.example.musicplayerlite.base.BaseFragment
import com.example.musicplayerlite.databinding.FragmentHomeBinding
import com.example.musicplayerlite.permission.PermissionCallback
import com.example.musicplayerlite.permission.PermissionHelper
import com.example.musicplayerlite.screen.playlist.SongViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.lang.ref.WeakReference

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel by activityViewModel<SongViewModel>()
    private lateinit var adapterActivities: MusicActivitiesAdapter
    private val permissionHelper: PermissionHelper = PermissionHelper()
    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requestPermissionLauncher = permissionHelper.permissionLauncher(
            fragment = WeakReference(this).get() ?: return,
            onGranted = {
                Log.d("tag123", "onGranted: ");
            },
            onDenied = {
                Log.d("tag123", "onDenied: ");
            })

    }

    override fun getViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun initData() {
        adapterActivities = MusicActivitiesAdapter()
    }

    override fun initView() {
        setUpRcv()
        permissionHelper.checkAndRequestPermission(requireContext(),
            Manifest.permission.READ_MEDIA_AUDIO,
            requestPermissionLauncher ?: return,
            object : PermissionCallback {
                override fun onGranted() {
                    Log.d("tag123", "PermissionCallback onGranted: ");
                }

                override fun onDenied() {
                    Log.d("tag123", "PermissionCallback onDenied: ");
                }

                override fun onRationale() {
                    Log.d("tag123", "PermissionCallback onRationale: ");
                }
            }
        )
    }

    override fun initViewListener() {
        adapterActivities.setOnActivitiesTypeSelected { type ->
            when (type) {
                PLAY_LIST -> {
                    navigateToPlayListScreen()
                }

                LIKE_SONG -> {}
                FOLLOWED_ARTIST -> {}
                YOUR_HISTORY -> {}
            }
        }

        with(binding.iclYourDownloads) {
            cvAlbum.setOnClickListener {
                navigateToAlbumScreen()
            }
            cvPlaylist.setOnClickListener {
                navigateToPlayListScreen()
            }
            cvArtist.setOnClickListener {
                navigateToArtistScreen()
            }
            cvVideo.setOnClickListener {
            }
        }
    }

    override fun initObserver() {
        with(binding.iclYourDownloads) {
            viewModel.albumsStateFlow.onEach { albums ->
                tvAlbumCount.text = getString(R.string.number_albums, albums.count())
            }.launchIn(lifecycleScope)
            viewModel.artistStateFlow.onEach { albums ->
                tvArtistCount.text = getString(R.string.number_artist, albums.count())
            }.launchIn(lifecycleScope)
            viewModel.songUiState.onEach { songs ->
                tvPlaylistCount.text = getString(R.string.number_song, songs.count())
            }.launchIn(lifecycleScope)
        }
    }

    private fun setUpRcv() {
        with(binding.iclYourActivities.rcvYourActivities) {
            adapter = adapterActivities
            itemAnimator = null
        }
    }

    private fun navigateToPlayListScreen() {
        findNavController().navigate(R.id.playListFragment)
    }

    private fun navigateToArtistScreen() {
        findNavController().navigate(R.id.artistFragment)
    }

    private fun navigateToAlbumScreen() {
        findNavController().navigate(R.id.albumFragment)
    }
}

