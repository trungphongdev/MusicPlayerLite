package com.example.musicplayerlite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringDef
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayerlite.R
import com.example.musicplayerlite.databinding.LayoutItemYourActivitiesBinding

const val PLAY_LIST = "PLAY_LIST"
const val LIKE_SONG = "LIKE_SONG"
const val FOLLOWED_ARTIST = "FOLLOWED_ARTIST"
const val YOUR_HISTORY = "YOUR_HISTORY"

@StringDef(
    value = [PLAY_LIST, LIKE_SONG, FOLLOWED_ARTIST, YOUR_HISTORY]
)
@Retention(AnnotationRetention.SOURCE)
annotation class UserActivities

typealias MusicUserActivities = Triple<String, Int, Int>

class MusicActivitiesAdapter :
    RecyclerView.Adapter<MusicActivitiesAdapter.MusicActivitiesViewHolder>() {
    private var dataResIds: List<MusicUserActivities> = getData()
    private var onItemSelected: (String) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicActivitiesViewHolder {
        val binding = LayoutItemYourActivitiesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MusicActivitiesViewHolder(binding)
    }

    override fun getItemCount(): Int = dataResIds.count()

    override fun onBindViewHolder(holder: MusicActivitiesViewHolder, position: Int) {
        val item = dataResIds[position]
        holder.onBind(item)
    }

    fun setOnActivitiesTypeSelected(typeSelected: (type: String) -> Unit) {
        onItemSelected = typeSelected
    }

    inner class MusicActivitiesViewHolder(
        private val binding: LayoutItemYourActivitiesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: MusicUserActivities) {
            val (type, nameResId, iconResId) = item
            binding.tvName.setText(nameResId)
            binding.imvIcon.setImageResource(iconResId)
            binding.root.setOnClickListener {
                val itemSelected = dataResIds[adapterPosition]
                onItemSelected.invoke(type)
            }
        }
    }

    private fun getData(): List<MusicUserActivities> {
        return buildList {
            add(Triple(PLAY_LIST, R.string.your_playlist, R.drawable.ic_disc_music))
            add(Triple(LIKE_SONG, R.string.like_songs, R.drawable.ic_video_v2))
            add(Triple(FOLLOWED_ARTIST, R.string.followed_artist, R.drawable.ic_heart))
            add(Triple(YOUR_HISTORY, R.string.history, R.drawable.ic_history_home))
        }
    }
}
