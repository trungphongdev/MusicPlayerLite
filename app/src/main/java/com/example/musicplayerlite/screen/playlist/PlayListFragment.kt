package com.example.musicplayerlite.screen.playlist

import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.musicplayerlite.MainActivity
import com.example.musicplayerlite.R
import com.example.musicplayerlite.adapter.SongAdapter
import com.example.musicplayerlite.base.BaseFragment
import com.example.musicplayerlite.databinding.FragmentPlayListBinding
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PlayListFragment : BaseFragment<FragmentPlayListBinding>() {
    private lateinit var adapterPlayList: SongAdapter
    private val viewModel by activityViewModel<SongViewModel>()

    override fun getViewBinding(): FragmentPlayListBinding {
        return FragmentPlayListBinding.inflate(layoutInflater)
    }

    override fun initData() {
        adapterPlayList = SongAdapter()
        adapterPlayList.setOnMoreSettings { }
    }

    override fun initView() {
        setUpRcvPlayList()
    }

    private fun setUpRcvPlayList() {
        binding.rcvPlayList.apply {
            adapter = adapterPlayList
            itemAnimator = null
            hasFixedSize()
        }
    }

    override fun initViewListener() {
        adapterPlayList.setOnSelectedSong { index ->
            (activity as MainActivity).listenerPlayback.onPositionChanged(index)
            navigateToPlaySongScreen()
        }

        adapterPlayList.setOnMoreSettings {
        }
    }

    private fun navigateToPlaySongScreen() {
        findNavController().navigate(R.id.playSongFragment)
    }

    override fun initObserver() {
        viewModel.songUiState.onEach { songs ->
            adapterPlayList.submitList(songs)
        }.launchIn(lifecycleScope)

        viewModel.musicStateFlow.filterNotNull().onEach { state ->
            adapterPlayList.setCurrentIndexSong(state.currentPos)
        }.launchIn(lifecycleScope)
    }

}