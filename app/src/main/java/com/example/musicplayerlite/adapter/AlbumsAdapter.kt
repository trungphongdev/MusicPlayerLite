package com.example.musicplayerlite.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayerlite.R
import com.example.musicplayerlite.databinding.LayoutItemAlbumBinding
import com.example.musicplayerlite.model.Album

class AlbumsAdapter : RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {
    val itemSize = 0.3 * Resources.getSystem().displayMetrics.widthPixels
    private var albums = emptyList<Album>()
    private var onAlbumIndexSelected: (index: Int) -> Unit = {}

    inner class AlbumViewHolder(private val binding: LayoutItemAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onAlbumIndexSelected.invoke(bindingAdapterPosition)
            }
            binding.imvAlbumArt.updateLayoutParams {
                width = itemSize.toInt()
                height = itemSize.toInt()
            }
        }

        fun onBind(album: Album) {
            with(binding) {
                tvAlbumsName.text = album.name
                tvArtist.text = album.artist
                tvAlbumsName.isSelected = true
                tvArtist.isSelected = true
                Glide.with(root.context)
                    .load(album.artworkUri)
                    .override(itemSize.toInt())
                    .placeholder(R.drawable.bg_album)
                    .into(imvAlbumArt)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            LayoutItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    fun setOnIndexAlbumSelected(onIndexSelected: (index: Int) -> Unit) {
        onAlbumIndexSelected = onIndexSelected
    }

    override fun getItemCount(): Int {
        return albums.count()
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.onBind(album = albums[position])
    }

    fun setData(albums: List<Album>) {
        this.albums = albums
        notifyDataSetChanged()
    }
}