package com.example.musicplayerlite.screen.album

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.musicplayerlite.adapter.AlbumsAdapter
import com.example.musicplayerlite.base.BaseFragment
import com.example.musicplayerlite.databinding.FragmentAlbumBinding
import com.example.musicplayerlite.screen.playlist.SongViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FragmentAlbums : BaseFragment<FragmentAlbumBinding>() {

    private val adapterAlbum: AlbumsAdapter = AlbumsAdapter()

    private val songViewModel by activityViewModel<SongViewModel>()
    override fun getViewBinding(): FragmentAlbumBinding {
        return FragmentAlbumBinding.inflate(layoutInflater)
    }

    override fun initData() {
        /* no-op */
    }

    override fun initView() {
        binding.rcvAlbum.apply {
            adapter = adapterAlbum
            itemAnimator = null
            setHasFixedSize(true)
        }
    }

    override fun initViewListener() {
        binding.imvBack.setOnClickListener {
            findNavController().navigateUp()
        }
        adapterAlbum.setOnIndexAlbumSelected {

        }
    }

    override fun initObserver() {
        songViewModel.albumsStateFlow.onEach { albums ->
            adapterAlbum.setData(albums)
        }.launchIn(lifecycleScope)
    }
}