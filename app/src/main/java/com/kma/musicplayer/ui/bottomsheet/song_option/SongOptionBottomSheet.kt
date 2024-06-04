package com.kma.musicplayer.ui.bottomsheet.song_option

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kma.musicplayer.R
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.databinding.BottomSheetSongOptionBinding
import com.kma.musicplayer.extension.showDialog
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.model.PlaylistModel
import com.kma.musicplayer.model.Song
import com.kma.musicplayer.service.PlaySongService
import com.kma.musicplayer.service.ServiceController
import com.kma.musicplayer.ui.bottomsheet.add_to_playlist.AddToPlaylistBottomSheet
import com.kma.musicplayer.utils.Formatter

class SongOptionBottomSheet(
    private val song: Song,
    private val onClickPlayNext: (() -> Unit)? = null,
    private val onClickAddToPlaylist: (() -> Unit)? = null,
    private val onClickHide: (() -> Unit)? = null,
    private val onClickShare: (() -> Unit)? = null,
    private val onClickDelete: (() -> Unit)? = null,
    private val onChangeFavorite: (() -> Unit)? = null
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetSongOptionBinding

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        binding = BottomSheetSongOptionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        initView()
        setupListeners()
    }

    private fun initView() {
        binding.tvName.text = song.title
        binding.tvDuration.text = "${Formatter.formatTime(song.duration.toLong())} mins"
        binding.ivFavourite.setImageResource(
            if (AppDatabase.INSTANCE.favouriteSongDao()
                    .isFavourite(song.id)
            ) R.drawable.ic_purple_heart else R.drawable.ic_white_heart
        )

        when (song) {
            is OnlineSong -> {
                binding.llDelete.visibility = View.GONE
                Glide.with(requireContext())
                    .load((song as OnlineSong).thumbnail)
                    .into(binding.ivThumbnail)
            }
            else -> {

            }
        }

        if (!ServiceController.isServiceRunning(requireContext(), PlaySongService::class.java)) {
            binding.llPlayNext.visibility = View.GONE
        }
    }

    private fun setupListeners() {
        binding.ivFavourite.setOnClickListener {
            if (AppDatabase.INSTANCE.favouriteSongDao().isFavourite(song.id)) {
                AppDatabase.INSTANCE.favouriteSongDao().delete(song.id)
                binding.ivFavourite.setImageResource(R.drawable.ic_white_heart)
            } else {
                AppDatabase.INSTANCE.favouriteSongDao().insert(song.id)
                binding.ivFavourite.setImageResource(R.drawable.ic_purple_heart)
            }
            onChangeFavorite?.invoke()
        }
        binding.llPlayNext.setOnClickListener {
            onClickPlayNext?.invoke()
        }
        binding.llAddToPlaylist.setOnClickListener {
            onClickAddToPlaylist?.invoke()
        }
        binding.llHide.setOnClickListener {
            onClickHide?.invoke()
        }
        binding.llShare.setOnClickListener {
            onClickShare?.invoke()
        }
        binding.llDelete.setOnClickListener {
            onClickDelete?.invoke()
        }
    }
}