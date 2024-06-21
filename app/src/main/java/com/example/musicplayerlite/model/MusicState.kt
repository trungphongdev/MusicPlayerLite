package com.example.musicplayerlite.model

import android.os.Parcelable
import com.example.musicplayerlite.common.Const
import kotlinx.parcelize.Parcelize

@Parcelize
data class MusicState(
    val currentPos: Int = Const.NO_POSITION,
    val song: Song? = null,
    val isPlaying: Boolean = false,
    val duration: Int = 0,
):Parcelable
