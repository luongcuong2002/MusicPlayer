package com.kma.musicplayer.ui.bottomsheet.playlist_option

import android.annotation.SuppressLint
import android.app.Dialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kma.musicplayer.R
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.databinding.BottomSheetPlaylistOptionBinding
import com.kma.musicplayer.extension.showDialog
import com.kma.musicplayer.model.PlaylistModel
import com.kma.musicplayer.service.PlaySongService
import com.kma.musicplayer.service.ServiceController

class PlaylistOptionBottomSheet(
    private val playlistModel: PlaylistModel,
    private val onPlaylistDeleted: () -> Unit,
    private val onClickPlay: () -> Unit,
    private val onClickAddToQueue: () -> Unit,
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetPlaylistOptionBinding

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        binding = BottomSheetPlaylistOptionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        initView()
        setupListeners()
    }

    private fun initView() {
        binding.tvName.text = playlistModel.playlistName
        val size = playlistModel.songIds.size
        binding.tvQuantity.text = if (size > 1) {
            "$size ${binding.root.context.getString(R.string.songs_2)}"
        } else {
            "$size ${binding.root.context.getString(R.string.song)}"
        }

        if (!ServiceController.isServiceRunning(requireActivity(), PlaySongService::class.java)) {
            binding.llAddToQueue.visibility = android.view.View.GONE
        }
    }

    private fun setupListeners() {
        binding.ivDelete.setOnClickListener {
            context?.showDialog(
                title = getString(R.string.delete_playlist),
                message = getString(R.string.delete_playlist_message),
                textOfNegativeButton = getString(R.string.cancel),
                textOfPositiveButton = getString(R.string.delete),
                positiveButtonFunction = {
                    AppDatabase.INSTANCE.playlistDao().deletePlaylist(playlistModel)
                    onPlaylistDeleted.invoke()
                    dismiss()
                }
            )
        }
        binding.llPlayAll.setOnClickListener {
            onClickPlay.invoke()
            dismiss()
        }
        binding.llAddToQueue.setOnClickListener {
            onClickAddToQueue.invoke()
            dismiss()
        }
    }
}