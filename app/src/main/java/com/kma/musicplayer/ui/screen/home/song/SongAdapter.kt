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
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.LayoutItemHomeSongBinding
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.model.Song
import com.kma.musicplayer.model.Theme
import com.kma.musicplayer.utils.Formatter

class SongAdapter(
    private val songs: List<Song>,
    private val onMoreClick: (Song) -> Unit,
    private val onSongClick: (Song) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private var theme: Theme = Theme.DARK

    fun setTheme(theme: Theme) {
        this.theme = theme
        notifyDataSetChanged()
    }

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
        fun bind(song: Song) {
            binding.tvTitle.text = song.title
            binding.tvArtistAndDuration.text = "${song.artist?.name  ?: binding.root.context.getString(R.string.unknown)} | ${Formatter.formatTime(song.duration.toLong())} mins"

            binding.progressBar.visibility = View.VISIBLE

            if (song is OnlineSong) {
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
            } else {
                binding.progressBar.visibility = View.GONE
                binding.ivThumbnail.setImageResource(R.drawable.default_song_thumbnail)
            }

            binding.ivMore.setOnClickListener {
                onMoreClick(song)
            }

            binding.root.setOnClickListener {
                onSongClick(song)
            }

            Log.d("CHECK_BUG", "bind: ${theme.titleTextColorRes} - theme: $theme")

            binding.tvTitle.setTextColor(binding.root.resources.getColor(theme.titleTextColorRes))
            binding.cardView.setCardBackgroundColor(binding.root.resources.getColor(theme.cardBackgroundColorRes))
            binding.ivMore.setImageResource(theme.imageMenuRes)
        }
    }
}