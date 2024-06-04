package com.kma.musicplayer.ui.screen.songselector

import android.content.ComponentName
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProvider
import com.kma.musicplayer.R
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.databinding.ActivitySongSelectorBinding
import com.kma.musicplayer.extension.showDialog
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.service.PlaySongService
import com.kma.musicplayer.service.ServiceController
import com.kma.musicplayer.ui.bottomsheet.add_to_playlist.AddToPlaylistBottomSheet
import com.kma.musicplayer.ui.screen.core.BaseActivity
import com.kma.musicplayer.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SongSelectorActivity : BaseActivity<ActivitySongSelectorBinding>() {

    private lateinit var songSelectorViewModel: SongSelectorViewModel
    private lateinit var selectableSongAdapter: SelectableSongAdapter

    override fun getContentView(): Int = R.layout.activity_song_selector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!ServiceController.isServiceRunning(this, PlaySongService::class.java)) {
            binding.llPlayNext.isEnabled = false
            binding.llPlayNext.alpha = 0.5f
        }

        initViewModel()
        setupRecyclerView()
        setupListeners()
        setupObserver()
    }

    private fun initViewModel() {
        songSelectorViewModel = ViewModelProvider(this)[SongSelectorViewModel::class.java]
        val songs = intent.getSerializableExtra(Constant.BUNDLE_SONGS) as List<OnlineSong>
        songSelectorViewModel.setSongs(songs)
    }

    private fun setupRecyclerView() {
        selectableSongAdapter = SelectableSongAdapter(songSelectorViewModel.songs) {
            songSelectorViewModel.songs[it].isSelected = !songSelectorViewModel.songs[it].isSelected
            selectableSongAdapter.notifyItemChanged(it)
            songSelectorViewModel.checkSelectAll()
            songSelectorViewModel.checkAtLeastOneSelected()
        }
        binding.rvSongs.adapter = selectableSongAdapter
    }

    private fun setupListeners() {
        binding.ivSelectAll.setOnClickListener {
            if (songSelectorViewModel.isSelectAll.value == true) {
                songSelectorViewModel.deselectAll()
            } else {
                songSelectorViewModel.selectAll()
            }
            selectableSongAdapter.notifyDataSetChanged()
        }
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                selectableSongAdapter.doFilter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.llAddTo.setOnClickListener {
            val selectedSongIds = songSelectorViewModel.getSelectedSongIds()
            val addToPlaylistBottomSheet = AddToPlaylistBottomSheet(selectedSongIds)
            addToPlaylistBottomSheet.show(supportFragmentManager, "AddToPlaylistBottomSheet")
        }
        binding.llHide.setOnClickListener {
            showDialog(
                title = getString(R.string.hide),
                message = getString(R.string.hide_songs_confirm),
                textOfPositiveButton = getString(R.string.hide),
                textOfNegativeButton = getString(R.string.cancel),
                positiveButtonFunction = {
                    CoroutineScope(Dispatchers.Main).launch {
                        val selectedSongIds = songSelectorViewModel.getSelectedSongIds()
                        AppDatabase.INSTANCE.hiddenSongDao().insertAll(selectedSongIds)
                        setResult(RESULT_OK)
                        songSelectorViewModel.hideSelectedSongs()

                        if (songSelectorViewModel.songs.isEmpty()) {
                            finish()
                        }

                        setupRecyclerView()
                    }
                }
            )
        }
        binding.llPlayNext.setOnClickListener {
            val selectedSongs = songSelectorViewModel.getSelectedSongs()
            songService?.songs?.addAll(selectedSongs)
        }
    }

    private fun setupObserver() {
        songSelectorViewModel.isSelectAll.observe(this) {
            binding.ivSelectAll.setImageResource(if (it) R.drawable.ic_select_all_enable else R.drawable.ic_select_all_disable)
        }
        songSelectorViewModel.isAtLeastOneSelected.observe(this) {
            val opacity = if (it) 1.0f else 0.5f
            binding.llHide.alpha = opacity
            binding.llHide.isClickable = it
            binding.llAddTo.alpha = opacity
            binding.llAddTo.isClickable = it
            if (ServiceController.isServiceRunning(this, PlaySongService::class.java)) {
                binding.llPlayNext.alpha = opacity
                binding.llPlayNext.isClickable = it
            }
        }
    }
}