package com.kma.musicplayer.ui.screen.home.playlist

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kma.musicplayer.R
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.databinding.LayoutItemPlaylistInfoWithMoreButtonBinding
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.model.PlaylistModel
import com.kma.musicplayer.model.Theme
import com.kma.musicplayer.utils.SongManager

class PlaylistAdapter(
    private val playlists: MutableList<PlaylistModel>,
    private val onClickMore: (PlaylistModel) -> Unit,
    private val onClickItem: (PlaylistModel) -> Unit,
) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    private var theme: Theme = Theme.DARK

    fun setTheme(theme: Theme) {
        this.theme = theme
        notifyDataSetChanged()
    }

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
            binding.tvName.text = playlists[adapterPosition].playlistName

            val size = playlists[adapterPosition].songIds.size
            binding.tvQuantity.text = if (size > 1) {
                "$size ${binding.root.context.getString(R.string.songs_2)}"
            } else {
                "$size ${binding.root.context.getString(R.string.song)}"
            }

            val firstSongId = playlists[adapterPosition].songIds.firstOrNull { !AppDatabase.INSTANCE.hiddenSongDao().isHidden(it) }
            firstSongId?.let {
                val firstSong = SongManager.getSongById(it)
                if (firstSong is OnlineSong) {
                    Glide.with(binding.root.context)
                        .load(firstSong.thumbnail)
                        .into(binding.ivThumbnail)
                } else {
                    binding.ivThumbnail.setImageResource(R.drawable.default_song_thumbnail)
                }
            }

            binding.ivMore.setOnClickListener {
                onClickMore(playlists[adapterPosition])
            }

            binding.root.setOnClickListener {
                onClickItem(playlists[adapterPosition])
            }

            binding.tvName.setTextColor(binding.root.resources.getColor(theme.titleTextColorRes))
            binding.cardView.setCardBackgroundColor(binding.root.resources.getColor(theme.cardBackgroundColorRes))
            binding.ivMore.setImageResource(theme.imageMenuRes)
        }
    }
}