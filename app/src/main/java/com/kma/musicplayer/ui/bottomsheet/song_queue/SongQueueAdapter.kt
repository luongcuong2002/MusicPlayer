package com.kma.musicplayer.ui.bottomsheet.song_queue

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kma.musicplayer.databinding.LayoutItemSongQueueBinding
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.model.Song

class SongQueueAdapter(
    private val songs: List<Song>
) : RecyclerView.Adapter<SongQueueAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutItemSongQueueBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = songs.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    inner class ViewHolder(val binding: LayoutItemSongQueueBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.tvName.text = song.title
            binding.tvArtist.text = song.artist

            if (song is OnlineSong) {
                Glide.with(binding.root)
                    .load(song.thumbnail)
                    .into(binding.ivThumbnail)
            }

            binding.root.setOnClickListener {

            }
        }
    }
}