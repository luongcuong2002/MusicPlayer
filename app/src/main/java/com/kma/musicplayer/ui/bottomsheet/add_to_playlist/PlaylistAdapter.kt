package com.kma.musicplayer.ui.bottomsheet.add_to_playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.LayoutItemPlaylistInfoBinding

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
            binding.tvName.text = playlists[adapterPosition].playlistModel.playlistName

            val size = playlists[adapterPosition].playlistModel.songIds.size
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

            binding.root.setOnClickListener {
                playlists[adapterPosition].isSelected = !playlists[adapterPosition].isSelected
                notifyItemChanged(adapterPosition)
            }
        }
    }
}