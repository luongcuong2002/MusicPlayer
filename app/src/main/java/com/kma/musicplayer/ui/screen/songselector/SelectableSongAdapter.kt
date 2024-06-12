package com.kma.musicplayer.ui.screen.songselector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.LayoutItemSelectableSongBinding
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.model.Theme
import com.kma.musicplayer.utils.Formatter

class SelectableSongAdapter(
    private val songs: MutableList<SelectableSong>,
    private val onSongClick: (Int) -> Unit
) : RecyclerView.Adapter<SelectableSongAdapter.SongViewHolder>() {

    private var theme = Theme.DARK
    private val tempSongs = mutableListOf<SelectableSong>()

    fun setTheme(theme: Theme) {
        this.theme = theme
        notifyDataSetChanged()
    }

    fun doFilter(text: String) {
        // If tempVideoPaths is empty, it means that the adapter has not been filtered yet
        if (tempSongs.isEmpty() && songs.isNotEmpty()) {
            tempSongs.addAll(songs)
        }

        val data = mutableListOf<SelectableSong>()

        if (text.isEmpty()) {
            data.addAll(tempSongs)
        } else {
            tempSongs.forEach {
                if (it.song.title.contains(text, ignoreCase = true)) {
                    data.add(it)
                }
            }
        }
        songs.clear()
        songs.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = LayoutItemSelectableSongBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = songs.size

    inner class SongViewHolder(val binding: LayoutItemSelectableSongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val song = songs[position].song
            binding.tvTitle.text = song.title
            binding.tvArtistAndDuration.text =
                "${song.artist.name} | ${Formatter.formatTime(song.duration.toLong())} mins"

            if (song is OnlineSong) {
                Glide.with(binding.root)
                    .load(song.thumbnail)
                    .into(binding.ivThumbnail)
            } else {
                binding.ivThumbnail.setImageResource(R.drawable.default_song_thumbnail)
            }

            binding.ivCheckbox.setImageResource(
                if (songs[position].isSelected) {
                    theme.imageCheckBoxSelectedRes
                } else {
                    theme.imageCheckBoxUnSelectedRes
                }
            )

            binding.root.setOnClickListener {
                onSongClick(position)
            }

            binding.tvTitle.setTextColor(binding.root.resources.getColor(theme.titleTextColorRes))
        }
    }
}