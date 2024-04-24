package com.example.musicplayerlite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayerlite.R
import com.example.musicplayerlite.databinding.LayoutItemSongBinding
import com.example.musicplayerlite.model.Song
import java.lang.ref.WeakReference

class SongAdapter : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    private var currentIndex = RecyclerView.NO_POSITION
    private var onSelectedIndexSong: (position: Int) -> Unit = {}
    private var onMoreSettings: () -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SongViewHolder(
        LayoutItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun getItemCount(): Int = listDiffer.currentList.count()

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = listDiffer.currentList[position]
        holder.bindView(song)
    }

    fun submitList(list: List<Song>) {
        listDiffer.submitList(list)
    }

    inner class SongViewHolder(private val binding: LayoutItemSongBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onSelectedIndexSong.invoke(adapterPosition)
            }
            binding.imvMore.setOnClickListener {
                onMoreSettings.invoke()
            }
        }

        fun bindView(song: Song) {
            with(binding) {
                val context = root.context
                tvSong.text = song.title
                tvArtist.text = song.getNameArtist(context)
                animViewSong.apply {
                    isVisible = currentIndex == adapterPosition
                    if (currentIndex == adapterPosition) playAnimation()
                    else cancelAnimation()
                }

                WeakReference<AppCompatImageView>(imvSong).get()?.let {
                    Glide.with(context)
                        .load(song.artworkUri)
                        .placeholder(R.drawable.ic_music)
                        .into(it)
                }
            }
        }
    }

    private val listDiffer = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem
        }
    })

    fun setOnSelectedSong(onSelectedIndex: (index: Int) -> Unit) {
        onSelectedIndexSong = onSelectedIndex
    }

    fun setOnMoreSettings(onMore: () -> Unit) {
        onMoreSettings = onMore
    }

    fun setCurrentIndexSong(index: Int) {
        notifyItemChanged(currentIndex)
        currentIndex = index
        notifyItemChanged(currentIndex)
    }

}