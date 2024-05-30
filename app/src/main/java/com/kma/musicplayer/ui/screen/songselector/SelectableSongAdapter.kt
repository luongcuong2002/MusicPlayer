package com.kma.musicplayer.ui.screen.songselector

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.LayoutItemSelectableSongBinding
import com.kma.musicplayer.utils.Formatter

class SelectableSongAdapter(
    private val songs: MutableList<SelectableSong>,
    private val onSongClick: (Int) -> Unit
) : RecyclerView.Adapter<SelectableSongAdapter.SongViewHolder>() {

    private val tempSongs = mutableListOf<SelectableSong>()

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
            binding.tvArtistAndDuration.text = "${song.artist} | ${Formatter.formatTime(song.duration.toLong())} mins"

            binding.progressBar.visibility = View.VISIBLE
            Glide.with(binding.root)
                .load(song.thumbnail)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("CHECK_BUG", e.toString())
                        return false
                    }
                })
                .into(binding.ivThumbnail)

            binding.ivCheckbox.setImageResource(
                if (songs[position].isSelected) {
                    R.drawable.ic_select_all_enable
                } else {
                    R.drawable.ic_checkbox_unselected
                }
            )

            binding.root.setOnClickListener {
                onSongClick(position)
            }
        }
    }
}