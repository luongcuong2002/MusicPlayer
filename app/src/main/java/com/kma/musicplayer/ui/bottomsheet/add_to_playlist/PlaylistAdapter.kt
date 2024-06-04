package com.kma.musicplayer.ui.bottomsheet.add_to_playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.LayoutItemPlaylistInfoBinding
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.utils.SongManager

class PlaylistAdapter(
    private val playlists: MutableList<SelectablePlaylist>
) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    fun refreshWithNewList(newList: List<SelectablePlaylist>) {
        playlists.clear()
        playlists.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutItemPlaylistInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(val binding: LayoutItemPlaylistInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val playlistModel = playlists[adapterPosition].playlistModel
            binding.tvName.text = playlistModel.playlistName

            val size = playlistModel.songIds.size
            binding.tvQuantity.text = if (size > 1) {
                "$size ${binding.root.context.getString(R.string.songs_2)}"
            } else {
                "$size ${binding.root.context.getString(R.string.song)}"
            }

            binding.rbSelect.setImageResource(
                if (playlists[adapterPosition].isSelected) {
                    R.drawable.ic_radio_checked
                } else {
                    R.drawable.ic_radio_unchecked
                }
            )

            (playlistModel.songIds.first {
                SongManager.getSongById(it) is OnlineSong
            } as OnlineSong).let {
                Glide.with(binding.root.context)
                    .load(it.thumbnail)
                    .into(binding.ivThumbnail)
            }

            binding.root.setOnClickListener {
                playlists[adapterPosition].isSelected = !playlists[adapterPosition].isSelected
                notifyItemChanged(adapterPosition)
            }
        }
    }
}