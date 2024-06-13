package com.kma.musicplayer.ui.screen.hiddensong

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProvider
import com.kma.musicplayer.databinding.ActivityHiddenSongBinding
import com.kma.musicplayer.ui.screen.core.BaseActivity
import com.kma.musicplayer.R
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.model.Theme
import com.kma.musicplayer.ui.screen.songselector.SelectableSongAdapter
import com.kma.musicplayer.utils.SongManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HiddenSongActivity : BaseActivity<ActivityHiddenSongBinding>() {

    private lateinit var hiddenSongViewModel: HiddenSongViewModel
    private var selectableSongAdapter: SelectableSongAdapter? = null

    override fun getContentView(): Int = R.layout.activity_hidden_song

    override fun onThemeChanged(theme: Theme) {
        binding.root.setBackgroundColor(resources.getColor(theme.backgroundColorRes))
        binding.backButton.setImageResource(theme.imageBackButtonRes)
        binding.tvTitle.setTextColor(resources.getColor(theme.titleTextColorRes))
        binding.cvSearch.setCardBackgroundColor(resources.getColor(theme.editTextBackgroundColorRes))
        binding.ivSearch.setImageResource(theme.imageSearchRes)
        binding.etSearch.setTextColor(resources.getColor(theme.titleTextColorRes))
        binding.ivSelectAll.setImageResource(if (hiddenSongViewModel.isSelectAll.value == true) theme.imageCheckBoxSelectedRes else theme.imageCheckBoxUnSelectedRes)
        binding.tvNoSongs.setTextColor(getColor(theme.titleTextColorRes))
        selectableSongAdapter?.setTheme(theme)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        setupListeners()
        setupObserver()
    }

    private fun initViewModel() {
        hiddenSongViewModel = ViewModelProvider(this)[HiddenSongViewModel::class.java]
        // val songs = intent.getSerializableExtra(Constant.BUNDLE_SONGS) as List<OnlineSong>
        CoroutineScope(Dispatchers.Main).launch {
            val songs = AppDatabase.INSTANCE.hiddenSongDao().getHiddenSongs().map {
                SongManager.getSongById(it.songId)
            }.filterNotNull()
            hiddenSongViewModel.setSongs(songs)
            setupRecyclerView()
        }
    }

    private fun setupRecyclerView() {
        selectableSongAdapter = SelectableSongAdapter(hiddenSongViewModel.songs) {
            hiddenSongViewModel.songs[it].isSelected = !hiddenSongViewModel.songs[it].isSelected
            selectableSongAdapter?.notifyItemChanged(it)
            hiddenSongViewModel.checkSelectAll()
            hiddenSongViewModel.checkAtLeastOneSelected()
        }
        binding.rvSongs.adapter = selectableSongAdapter
    }

    private fun setupListeners() {
        binding.ivSelectAll.setOnClickListener {
            if (hiddenSongViewModel.isSelectAll.value == true) {
                hiddenSongViewModel.deselectAll()
            } else {
                hiddenSongViewModel.selectAll()
            }
            selectableSongAdapter?.notifyDataSetChanged()
        }
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                selectableSongAdapter?.doFilter(s.toString())
                hiddenSongViewModel.checkListEmpty()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.llUnhide.setOnClickListener {
            val selectedSongs = hiddenSongViewModel.getSelectedSongs()
            CoroutineScope(Dispatchers.Main).launch {
                AppDatabase.INSTANCE.hiddenSongDao().deleteAll(selectedSongs.map { it.id })
                finish()
            }
        }
    }

    private fun setupObserver() {
        hiddenSongViewModel.isSelectAll.observe(this) {
            binding.ivSelectAll.setImageResource(if (it) getThemeViewModel().theme.value!!.imageCheckBoxSelectedRes else getThemeViewModel().theme.value!!.imageCheckBoxUnSelectedRes)
        }
        hiddenSongViewModel.isAtLeastOneSelected.observe(this) {
            val opacity = if (it) 1.0f else 0.5f
            binding.llUnhide.alpha = opacity
            binding.llUnhide.isClickable = it
        }
        hiddenSongViewModel.isListEmpty.observe(this) {
            binding.tvNoSongs.visibility = if (it) android.view.View.VISIBLE else android.view.View.GONE
            binding.rvSongs.visibility = if (it) android.view.View.GONE else android.view.View.VISIBLE
        }
    }
}