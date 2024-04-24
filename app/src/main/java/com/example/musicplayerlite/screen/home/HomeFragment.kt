package com.example.musicplayerlite.screen.home

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
import com.example.musicplayerlite.screen.playlist.SongViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel by activityViewModel<SongViewModel>()
    private lateinit var adapterActivities: MusicActivitiesAdapter


    private fun setUpRcv() {
        with(binding.iclYourActivities.rcvYourActivities) {
            adapter = adapterActivities
            itemAnimator = null
        }
    }

    override fun getViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun initData() {
        adapterActivities = MusicActivitiesAdapter()
    }

    override fun initView() {
        setUpRcv()
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

    private fun navigateToPlayListScreen() {
        findNavController().navigate(R.id.playListFragment)
    }
}

