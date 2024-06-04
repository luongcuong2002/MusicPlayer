package com.kma.musicplayer.ui.bottomsheet.song_queue

import android.annotation.SuppressLint
import android.app.Dialog
import android.text.TextWatcher
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kma.musicplayer.R
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.databinding.BottomSheetCreateNewPlaylistBinding
import com.kma.musicplayer.databinding.BottomSheetSongQueueBinding
import com.kma.musicplayer.model.PlaylistModel
import com.kma.musicplayer.model.Song
import com.kma.musicplayer.ui.bottomsheet.add_to_playlist.PlaylistAdapter
import com.kma.musicplayer.ui.customview.CenterLayoutManager
import com.kma.musicplayer.ui.customview.VerticalSpaceItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongQueueBottomSheet(
    private val songs: MutableList<Song>,
    private val playingSongIndex: Int
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetSongQueueBinding
    private lateinit var songQueueAdapter: SongQueueAdapter

    private val maxVisibleItems = 4
    private var itemHeight: Int = 100
    private var spaceBetweenItems = 100

    private val tempSongs = mutableListOf<Song>().apply {
        addAll(songs)
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        itemHeight = requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._47sdp).toInt()
        spaceBetweenItems =
            requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._13sdp).toInt()

        binding = BottomSheetSongQueueBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.tvQueue.text = getString(R.string.queue) + " (${songs.size})"

        setupRecycleView()
        setupListeners()
    }

    private fun setupRecycleView() {
        CoroutineScope(Dispatchers.Main).launch {
            songQueueAdapter = SongQueueAdapter(tempSongs, playingSongIndex)

            binding.rvSongQueue.addItemDecoration(VerticalSpaceItemDecoration(spaceBetweenItems))
            binding.rvSongQueue.layoutManager = CenterLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            binding.rvSongQueue.adapter = songQueueAdapter

            calculateRecyclerViewHeight()

            // swap
            val itemTouchHelper = ItemTouchHelper(MyItemTouchHelperCallback(songQueueAdapter))
            itemTouchHelper.attachToRecyclerView(binding.rvSongQueue)
        }
    }

    private fun calculateRecyclerViewHeight() {
        val totalItems: Int = songs.size

        if (totalItems == 0) {
            return
        }

        val recyclerViewHeight = if (totalItems < maxVisibleItems) {
            totalItems * itemHeight + (totalItems - 1) * spaceBetweenItems
        } else {
            maxVisibleItems * itemHeight + (maxVisibleItems - 1) * spaceBetweenItems
        }
        val layoutParams: ViewGroup.LayoutParams = binding.rvSongQueue.layoutParams
        layoutParams.height = recyclerViewHeight
        binding.rvSongQueue.layoutParams = layoutParams
    }

    private fun setupListeners() {

        binding.rlCancel.setOnClickListener {
            dismiss()
        }

        binding.rlOk.setOnClickListener {
            songs.clear()
            songs.addAll(tempSongs)
            dismiss()
        }
    }
}