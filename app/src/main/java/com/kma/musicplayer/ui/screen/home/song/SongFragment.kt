package com.kma.musicplayer.ui.screen.home.song

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.FragmentSongBinding
import com.kma.musicplayer.network.common.ApiCallState
import com.kma.musicplayer.ui.screen.core.BaseFragment
import com.kma.musicplayer.ui.screen.songselector.SongSelectorActivity
import com.kma.musicplayer.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

class SongFragment : BaseFragment<FragmentSongBinding>() {

    private lateinit var songViewModel: SongViewModel
    private lateinit var songAdapter: SongAdapter

    override fun getContentView(): Int = R.layout.fragment_song

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songViewModel = ViewModelProvider(requireActivity()).get(SongViewModel::class.java)
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
        songAdapter = SongAdapter(songViewModel.songs) {
            // handle song click
        }
        binding.rvSongs.adapter = songAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.REQUEST_CODE_DATA_CHANGED && resultCode == Activity.RESULT_OK) {
            CoroutineScope(Dispatchers.Main).launch {
                songViewModel.filterHiddenSongs()
                songAdapter.notifyDataSetChanged()
            }
        }
    }
}