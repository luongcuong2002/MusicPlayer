package com.kma.musicplayer.ui.screen.home.playlist

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.LayoutItemPlaylistInfoWithMoreButtonBinding
import com.kma.musicplayer.model.PlaylistModel

class PlaylistAdapter(
    private val playlists: MutableList<PlaylistModel>,
    private val onClickMore: (PlaylistModel) -> Unit,
    private val onClickItem: (PlaylistModel) -> Unit,
) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutItemPlaylistInfoWithMoreButtonBinding.inflate(
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

    inner class ViewHolder(val binding: LayoutItemPlaylistInfoWithMoreButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {

            Log.d("PlaylistAdapter", "bind: ${playlists[adapterPosition]}")

            binding.tvName.text = playlists[adapterPosition].playlistName

            val size = playlists[adapterPosition].songIds.size
            binding.tvQuantity.text = if (size > 1) {
                "$size ${binding.root.context.getString(R.string.songs_2)}"
            } else {
                "$size ${binding.root.context.getString(R.string.song)}"
            }

            binding.ivMore.setOnClickListener {
                onClickMore(playlists[adapterPosition])
            }

            binding.root.setOnClickListener {
                onClickItem(playlists[adapterPosition])
            }
        }
    }
}