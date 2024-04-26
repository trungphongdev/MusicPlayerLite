package com.example.musicplayerlite.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayerlite.R
import com.example.musicplayerlite.databinding.LayoutItemArtistBinding
import com.example.musicplayerlite.model.Artist

class ArtistAdapter : RecyclerView.Adapter<ArtistAdapter.AlbumViewHolder>() {
    val itemSize = 0.3 * Resources.getSystem().displayMetrics.widthPixels
    private var artists = emptyList<Artist>()
    private var onAlbumIndexSelected: (index: Int) -> Unit = {}

    inner class AlbumViewHolder(private val binding: LayoutItemArtistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onAlbumIndexSelected.invoke(adapterPosition)
            }
            binding.imvArtist.updateLayoutParams {
                width = itemSize.toInt()
                height = itemSize.toInt()
            }
        }

        fun onBind(artist: Artist) {
            with(binding) {
                tvArtist.text = artist.name
                tvNumber.text = root.context.getString(R.string.number_song, artist.songs.count())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            LayoutItemArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    fun setOnIndexAlbumSelected(onIndexSelected: (index: Int) -> Unit) {
        onAlbumIndexSelected = onIndexSelected
    }

    override fun getItemCount(): Int {
        return artists.count()
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.onBind(artist = artists[position])
    }

    fun setData(artists: List<Artist>) {
        this.artists = artists
        notifyDataSetChanged()
    }
}