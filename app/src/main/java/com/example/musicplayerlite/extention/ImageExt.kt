package com.example.musicplayerlite.extention

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.musicplayerlite.R

fun ImageView.loadImage(any: Any) {
    Glide.with(this.context)
        .load(any)
        .centerCrop()
        .skipMemoryCache(true)
        .placeholder(R.drawable.ic_song_placeholder)
        .error(R.drawable.ic_song_placeholder)
        .into(this)
}