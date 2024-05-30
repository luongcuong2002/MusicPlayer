package com.kma.musicplayer.ui.bottomsheet.add_to_playlist

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kma.musicplayer.database.PlaylistDao
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.ui.bottomsheet.create_new_playlist.CreateNewPlaylistBottomSheet
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.BottomSheetAddToPlaylistBinding
import com.kma.musicplayer.ui.customview.VerticalSpaceItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddToPlaylistBottomSheet(
    private val songIdsToAdd: List<String>
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAddToPlaylistBinding

    private val maxVisibleItems = 4
    private var itemHeight: Int = 100
    private var spaceBetweenItems = 100

    private var playlistDao: PlaylistDao? = null

    private var playlists = mutableListOf<SelectablePlaylist>()
    private var playlistAdapter: PlaylistAdapter? = null

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        itemHeight = requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._47sdp).toInt()
        spaceBetweenItems =
            requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._13sdp).toInt()

        binding = BottomSheetAddToPlaylistBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        connectToDatabase()
        setupRecycleView()
        setupListeners()
    }

    private fun connectToDatabase() {
        playlistDao = AppDatabase.getInstance(requireActivity())?.playlistDao()
    }

    private fun setupRecycleView() {
        CoroutineScope(Dispatchers.Main).launch {

            playlists = fetchAllPlaylists()
            playlistAdapter = PlaylistAdapter(playlists)

            binding.rvPlaylist.addItemDecoration(VerticalSpaceItemDecoration(spaceBetweenItems))
            binding.rvPlaylist.layoutManager = LinearLayoutManager(requireActivity())
            binding.rvPlaylist.adapter = playlistAdapter

            calculateRecyclerViewHeight()
        }
    }

    private suspend fun fetchAllPlaylists(): MutableList<SelectablePlaylist> =
        withContext(Dispatchers.IO) {
            return@withContext playlistDao?.getAllPlaylists()
                ?.map {
                    SelectablePlaylist(it)
                }?.toMutableList() ?: mutableListOf()
        }

    private fun setupListeners() {
        binding.llNewPlaylist.setOnClickListener {
            val createNewPlaylistBottomSheet = CreateNewPlaylistBottomSheet() {
                CoroutineScope(Dispatchers.Main).launch {
                    playlists = fetchAllPlaylists()
                    playlistAdapter?.refreshWithNewList(playlists)
                    calculateRecyclerViewHeight()
                }
            }
            createNewPlaylistBottomSheet.show(
                requireActivity().supportFragmentManager,
                "CreateNewPlaylistBottomSheet"
            )
        }

        binding.rlSave.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                saveToSelectedPlaylists {
                    dismiss()
                }
            }
        }

        binding.rlCancel.setOnClickListener {
            dismiss()
        }
    }

    private suspend fun saveToSelectedPlaylists(onSaveSuccessfully: () -> Unit) =
        withContext(Dispatchers.IO) {
            val selectedPlaylists = playlists.filter { it.isSelected }

            if (selectedPlaylists.isEmpty()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireActivity(),
                        requireActivity().getString(R.string.please_select_at_least_one_playlist),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@withContext
            }

            var isAddedToAnyPlaylist = false

            selectedPlaylists.forEach { selectedPlaylist ->
                val id = selectedPlaylist.playlistModel.id
                val songIds = selectedPlaylist.playlistModel.songIds

                // check if the media path is already existed in the playlist
                if (songIds.any { songIdsToAdd.contains(it) }) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireActivity(),
                            "${requireActivity().getString(R.string.song_already_exists_in_playlist)} ${selectedPlaylist.playlistModel.playlistName}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return@forEach
                }

                val newMediaPaths = mutableListOf<String>().apply {
                    addAll(songIds)
                    addAll(songIdsToAdd)
                }
                playlistDao?.updateMediaPaths(id, newMediaPaths)

                isAddedToAnyPlaylist = true
            }

            if (isAddedToAnyPlaylist) {
                onSaveSuccessfully.invoke()
            }
        }

    private fun calculateRecyclerViewHeight() {
        val totalItems: Int = playlists.size

        if (totalItems == 0) {
            return
        }

        val recyclerViewHeight = if (totalItems < maxVisibleItems) {
            totalItems * itemHeight + (totalItems - 1) * spaceBetweenItems
        } else {
            maxVisibleItems * itemHeight + (maxVisibleItems - 1) * spaceBetweenItems
        }
        val layoutParams: ViewGroup.LayoutParams = binding.rvPlaylist.layoutParams
        layoutParams.height = recyclerViewHeight
        binding.rvPlaylist.layoutParams = layoutParams
    }
}