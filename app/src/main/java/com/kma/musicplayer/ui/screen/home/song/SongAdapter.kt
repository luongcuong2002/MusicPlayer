package com.kma.musicplayer.ui.screen.home.song

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
import com.kma.musicplayer.databinding.LayoutItemHomeSongBinding
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.utils.Formatter

class SongAdapter(
    private val songs: List<OnlineSong>,
    private val onSongClick: (OnlineSong) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = LayoutItemHomeSongBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount(): Int = songs.size

    inner class SongViewHolder(val binding: LayoutItemHomeSongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: OnlineSong) {
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

            binding.root.setOnClickListener {
                onSongClick(song)
            }
        }
    }
}