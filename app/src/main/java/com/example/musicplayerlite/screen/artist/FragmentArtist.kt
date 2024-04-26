package com.example.musicplayerlite.screen.artist

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.musicplayerlite.adapter.ArtistAdapter
import com.example.musicplayerlite.base.BaseFragment
import com.example.musicplayerlite.databinding.FragmentArtistBinding
import com.example.musicplayerlite.screen.playlist.SongViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FragmentArtist : BaseFragment<FragmentArtistBinding>() {

    private val adapterArtist: ArtistAdapter = ArtistAdapter()

    private val songViewModel by activityViewModel<SongViewModel>()
    override fun getViewBinding(): FragmentArtistBinding {
        return FragmentArtistBinding.inflate(layoutInflater)
    }

    override fun initData() {
        /* no-op */
    }

    override fun initView() {
        binding.rcvArtist.apply {
            adapter = adapterArtist
            itemAnimator = null
            setHasFixedSize(true)
        }
    }

    override fun initViewListener() {
        binding.imvBack.setOnClickListener {
            findNavController().navigateUp()
        }
        adapterArtist.setOnIndexAlbumSelected {

        }
    }

    override fun initObserver() {
        songViewModel.artistStateFlow.onEach { artist ->
            adapterArtist.setData(artist)
        }.launchIn(lifecycleScope)
    }
}