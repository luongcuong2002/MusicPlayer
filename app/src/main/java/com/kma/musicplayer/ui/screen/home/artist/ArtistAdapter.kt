package com.kma.musicplayer.ui.screen.home.artist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.LayoutItemPlaylistInfoWithMoreButtonBinding
import com.kma.musicplayer.model.Artist
import com.kma.musicplayer.model.Theme
import com.kma.musicplayer.utils.SongManager

class ArtistAdapter(
    private val artists: MutableList<Artist>,
    private val onClickMore: (Artist) -> Unit,
    private val onClickItem: (Artist) -> Unit,
) : RecyclerView.Adapter<ArtistAdapter.ViewHolder>() {

    private var theme = Theme.DARK

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

    override fun getItemCount(): Int = artists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(artists[position])
    }

    inner class ViewHolder(val binding: LayoutItemPlaylistInfoWithMoreButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: Artist) {
            binding.tvName.text = artist.name

            val size = SongManager.getSongByArtist(artist).size
            binding.tvQuantity.text = if (size > 1) {
                "$size ${binding.root.context.getString(R.string.songs_2)}"
            } else {
                "$size ${binding.root.context.getString(R.string.song)}"
            }

            Glide.with(binding.root.context)
                .load(artist.thumbnail)
                .into(binding.ivThumbnail)

            binding.ivMore.setOnClickListener {
                onClickMore(artists[adapterPosition])
            }

            binding.root.setOnClickListener {
                onClickItem(artists[adapterPosition])
            }

            binding.tvName.setTextColor(binding.root.resources.getColor(theme.titleTextColorRes))
            binding.cardView.setCardBackgroundColor(binding.root.resources.getColor(theme.cardBackgroundColorRes))
            binding.ivMore.setImageResource(theme.imageMenuRes)
        }
    }
}