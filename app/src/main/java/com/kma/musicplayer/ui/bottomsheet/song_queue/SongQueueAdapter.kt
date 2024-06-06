package com.kma.musicplayer.ui.bottomsheet.song_queue

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.LayoutItemSongQueueBinding
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.model.Song
import java.util.Collections

class SongQueueAdapter(
    private val songs: List<Song>,
    private val currentIndex: Int,
) : RecyclerView.Adapter<SongQueueAdapter.ViewHolder>() {

    private var recyclerView: RecyclerView? = null

    private var _firstVisibleItemWhenViewFromCurrentPosition = 0
    private var _lastVisibleItemWhenViewFromCurrentPosition = 0
    private var _currentPosition = currentIndex

    val currentPosition: Int
        get() = _currentPosition

    fun updateCurrentPosition(position: Int) {

        if (position == _currentPosition) return

        recyclerView?.let {
            _firstVisibleItemWhenViewFromCurrentPosition =
                (it.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            _lastVisibleItemWhenViewFromCurrentPosition =
                (it.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }

        // Update current position and notify data set changed
        notifyItemChanged(_currentPosition)
        _currentPosition = position
        notifyItemChanged(_currentPosition)
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(songs, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)

        // update current position
        if (_currentPosition == fromPosition) {
            updateCurrentPosition(toPosition)
        } else if (_currentPosition == toPosition) {
            updateCurrentPosition(fromPosition)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

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
            binding.tvArtist.text = song.artist.name

            if (song is OnlineSong) {
                Glide.with(binding.root)
                    .load(song.thumbnail)
                    .into(binding.ivThumbnail)
            }

            if (adapterPosition == _currentPosition) {
                binding.tvName.setTextColor(binding.root.context.getColor(R.color.color_7150D0))
                binding.tvArtist.setTextColor(binding.root.context.getColor(R.color.color_7150D0))
                binding.ivQueue.setImageResource(R.drawable.ic_purple_queue)
            } else {
                binding.tvName.setTextColor(binding.root.context.getColor(R.color.white))
                binding.tvArtist.setTextColor(binding.root.context.getColor(R.color.color_787B82))
                binding.ivQueue.setImageResource(R.drawable.ic_queue)
            }
        }
    }
}