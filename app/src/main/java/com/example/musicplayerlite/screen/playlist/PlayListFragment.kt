package com.example.musicplayerlite.screen.playlist

import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.musicplayerlite.MainActivity
import com.example.musicplayerlite.R
import com.example.musicplayerlite.adapter.SongAdapter
import com.example.musicplayerlite.base.BaseFragment
import com.example.musicplayerlite.databinding.FragmentPlayListBinding
import com.example.musicplayerlite.permission.PermissionCallback
import com.example.musicplayerlite.permission.PermissionHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.lang.ref.WeakReference

class PlayListFragment : BaseFragment<FragmentPlayListBinding>() {
    private lateinit var adapterPlayList: SongAdapter
    private val viewModel by activityViewModel<SongViewModel>()
    private val permissionHelper: PermissionHelper by inject()
    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null
    private var permissionMediaIsGranted = MutableStateFlow(false)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        permissionMediaIsGranted.update {
            permissionHelper.isPermissionGranted(context, permissionHelper.readMediaAudioPermission)
        }
        requestPermissionLauncher = permissionHelper.permissionLauncher(
            fragment = WeakReference(this).get() ?: return,
            onGranted = {
                permissionMediaIsGranted.update { true }
            },
            onDenied = {
                permissionMediaIsGranted.update { false }
            })
    }

    override fun getViewBinding(): FragmentPlayListBinding {
        return FragmentPlayListBinding.inflate(layoutInflater)
    }

    override fun initData() {
        adapterPlayList = SongAdapter()
        adapterPlayList.setOnMoreSettings { }
        checkAndRequestReadMediaPermission()
    }

    override fun initView() {
        setUpRcvPlayList()
    }

    override fun initObserver() {
        viewModel.songUiState.onEach { songs ->
            if (songs.isEmpty()) {
                binding.rcvPlayList.isVisible = false
                binding.groupViewEmptyData.isVisible = true
            } else {
                binding.rcvPlayList.isVisible = true
                binding.groupViewEmptyData.isVisible = false
                adapterPlayList.submitList(songs)
            }
        }.launchIn(lifecycleScope)

        viewModel.musicStateFlow.filterNotNull().onEach { state ->
            adapterPlayList.setCurrentIndexSong(state.currentPos)
        }.launchIn(lifecycleScope)

        permissionMediaIsGranted.onEach {
            // show view empty
        }
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
        binding.imvBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun checkAndRequestReadMediaPermission() {
        permissionHelper.checkAndRequestPermission(requireContext(),
            permission = permissionHelper.readMediaAudioPermission,
            requestPermissionLauncher ?: return,
            object : PermissionCallback {
                override fun onGranted() {
                    permissionMediaIsGranted.update { true }
                }

                override fun onDenied() {
                    permissionMediaIsGranted.update { false }
                }

                override fun onRationale() {
                    permissionMediaIsGranted.update { false }
                }
            }
        )
    }

    private fun navigateToPlaySongScreen() {
        findNavController().navigate(R.id.playSongFragment)
    }

}