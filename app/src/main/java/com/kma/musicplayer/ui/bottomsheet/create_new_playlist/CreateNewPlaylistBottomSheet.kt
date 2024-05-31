package com.kma.musicplayer.ui.bottomsheet.create_new_playlist

import android.annotation.SuppressLint
import android.app.Dialog
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kma.musicplayer.R
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.databinding.BottomSheetCreateNewPlaylistBinding
import com.kma.musicplayer.model.PlaylistModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateNewPlaylistBottomSheet(
    private val onPlaylistCreated: () -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetCreateNewPlaylistBinding

    private var playlistDao = AppDatabase.INSTANCE.playlistDao()

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        binding = BottomSheetCreateNewPlaylistBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {

        binding.rlCancel.setOnClickListener {
            dismiss()
        }

        binding.rlOk.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if (validatePlaylistName()) {
                    insertPlaylist(binding.edtPlaylistName.text.trim().toString())
                    onPlaylistCreated.invoke()
                    dismiss()
                }
            }
        }

        binding.edtPlaylistName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == null || s.trim().toString().isEmpty()) {
                    binding.rlOk.alpha = 0.4f
                } else {
                    binding.rlOk.alpha = 1f
                }
            }

            override fun afterTextChanged(s: android.text.Editable?) {

            }
        })
    }

    private suspend fun validatePlaylistName(): Boolean {

        val text = binding.edtPlaylistName.text.trim().toString()

        if (text.isEmpty()) {
            return false
        }

        if (checkPlaylistNameExisted(text)) {
            Toast.makeText(requireActivity(), getString(R.string.playlist_name_already_exists), Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private suspend fun checkPlaylistNameExisted(playlistName: String): Boolean =
        withContext(Dispatchers.IO) {
            return@withContext playlistDao.isPlaylistNameExisted(playlistName)
        }

    private suspend fun insertPlaylist(playlistName: String) = withContext(Dispatchers.IO) {
        val playlist = PlaylistModel(
            playlistName = playlistName
        )
        playlistDao.insertPlaylist(playlist)
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }
}