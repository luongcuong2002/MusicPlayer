package com.kma.musicplayer.ui.bottomsheet.artist_option

import android.annotation.SuppressLint
import android.app.Dialog
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kma.musicplayer.R
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.databinding.BottomSheetArtistOptionBinding
import com.kma.musicplayer.databinding.BottomSheetPlaylistOptionBinding
import com.kma.musicplayer.extension.showDialog
import com.kma.musicplayer.model.Artist
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.model.PlaylistModel
import com.kma.musicplayer.service.PlaySongService
import com.kma.musicplayer.service.ServiceController
import com.kma.musicplayer.ui.bottomsheet.add_to_playlist.AddToPlaylistBottomSheet
import com.kma.musicplayer.utils.SongManager

class ArtistOptionBottomSheet(
    private val artist: Artist,
    private val onClickPlay: () -> Unit,
    private val onClickAddToPlaylist: () -> Unit,
    private val onClickAddToQueue: () -> Unit,
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetArtistOptionBinding

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        binding = BottomSheetArtistOptionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        initView()
        setupListeners()
    }

    private fun initView() {
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

        if (!ServiceController.isServiceRunning(requireActivity(), PlaySongService::class.java)) {
            binding.llAddToQueue.visibility = android.view.View.GONE
        }
    }

    private fun setupListeners() {
        binding.llPlayAll.setOnClickListener {
            onClickPlay.invoke()
            dismiss()
        }
        binding.llAddToPlaylist.setOnClickListener {
            onClickAddToPlaylist.invoke()
            dismiss()
        }
        binding.llAddToQueue.setOnClickListener {
            onClickAddToQueue.invoke()
            dismiss()
        }
    }
}