package com.kma.musicplayer.ui.screen.home.song

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.kma.musicplayer.R
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.databinding.FragmentSongBinding
import com.kma.musicplayer.model.Theme
import com.kma.musicplayer.network.common.ApiCallState
import com.kma.musicplayer.ui.bottomsheet.add_to_playlist.AddToPlaylistBottomSheet
import com.kma.musicplayer.ui.bottomsheet.song_option.SongOptionBottomSheet
import com.kma.musicplayer.ui.screen.core.BaseFragment
import com.kma.musicplayer.ui.screen.playsong.PlaySongActivity
import com.kma.musicplayer.ui.screen.songselector.SongSelectorActivity
import com.kma.musicplayer.utils.Constant
import com.kma.musicplayer.utils.ShareUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

class SongFragment : BaseFragment<FragmentSongBinding>() {

    private lateinit var songViewModel: SongViewModel
    private var songAdapter: SongAdapter? = null

    override fun getContentView(): Int = R.layout.fragment_song

    override fun onThemeChanged(theme: Theme) {
        binding.tvAllSongs.setTextColor(resources.getColor(theme.adapterTitleTextColorRes))
        binding.ivCheckbox.setImageResource(theme.imageCheckBoxRes)
        binding.ivSort.setImageResource(theme.imageSortRes)
        binding.tvError.setTextColor(resources.getColor(theme.titleTextColorRes))
        songAdapter?.setTheme(theme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songViewModel = ViewModelProvider(requireActivity()).get(SongViewModel::class.java)
        songViewModel.fetAllSongs()
        setupListeners()
        setupObserver()
    }

    private fun setupListeners() {
        binding.ivCheckbox.setOnClickListener {
            showActivityForResult(
                SongSelectorActivity::class.java, Constant.REQUEST_CODE_DATA_CHANGED,
                Bundle().apply {
                    putSerializable(Constant.BUNDLE_SONGS, songViewModel.songs as Serializable)
                },
            )
        }
    }

    private fun setupObserver() {
        songViewModel.apiCallState.observe(viewLifecycleOwner) {
            when (it) {
                ApiCallState.LOADING -> {
                    binding.rvSongs.visibility = View.GONE
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                }

                ApiCallState.SUCCESS -> {
                    binding.rvSongs.visibility = View.VISIBLE
                    binding.pbLoading.visibility = View.GONE
                    binding.tvError.visibility = View.GONE
                    setupRecyclerView()
                    requestUpdateTheme()
                }

                ApiCallState.ERROR -> {
                    binding.rvSongs.visibility = View.GONE
                    binding.pbLoading.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupRecyclerView() {
        songAdapter = SongAdapter(
            songs = songViewModel.songs,
            onMoreClick = { song ->
                var bottomSheet: SongOptionBottomSheet? = null
                bottomSheet = SongOptionBottomSheet(
                    song = song,
                    onClickPlayNext = {
                        getBaseActivity().songService?.songs?.add(song)
                        bottomSheet?.dismiss()
                    },
                    onClickAddToPlaylist = {
                        val addToPlaylistBottomSheet = AddToPlaylistBottomSheet(listOf(song.id))
                        addToPlaylistBottomSheet.show(
                            requireActivity().supportFragmentManager,
                            addToPlaylistBottomSheet.tag
                        )
                        bottomSheet?.dismiss()
                    },
                    onClickHide = {
                        CoroutineScope(Dispatchers.Main).launch {
                            AppDatabase.INSTANCE.hiddenSongDao().insert(song.id)
                            songViewModel.filterHiddenSongs()
                            songAdapter?.notifyDataSetChanged()
                            bottomSheet?.dismiss()
                        }
                    },
                    onClickShare = {
                        ShareUtils.shareSong(requireActivity(), song)
                        bottomSheet?.dismiss()
                    },
                )
                bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
            },
            onSongClick = { song ->
                showActivity(
                    PlaySongActivity::class.java,
                    Bundle().apply {
                        putSerializable(Constant.BUNDLE_SONGS, mutableListOf(song) as Serializable)
                    },
                )
            },
        )
        binding.rvSongs.adapter = songAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.REQUEST_CODE_DATA_CHANGED && resultCode == Activity.RESULT_OK) {
            CoroutineScope(Dispatchers.Main).launch {
                songViewModel.filterHiddenSongs()
                songAdapter?.notifyDataSetChanged()
            }
        }
    }
}