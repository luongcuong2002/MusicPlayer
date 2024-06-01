package com.kma.musicplayer.ui.screen.home.playlist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.FragmentPlaylistBinding
import com.kma.musicplayer.ui.bottomsheet.create_new_playlist.CreateNewPlaylistBottomSheet
import com.kma.musicplayer.ui.bottomsheet.playlist_option.PlaylistOptionBottomSheet
import com.kma.musicplayer.ui.customview.VerticalSpaceItemDecoration
import com.kma.musicplayer.ui.screen.core.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistFragment : BaseFragment<FragmentPlaylistBinding>() {

    private lateinit var playlistViewModel: PlaylistViewModel
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun getContentView(): Int = R.layout.fragment_playlist

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistViewModel = ViewModelProvider(this)[PlaylistViewModel::class.java]

        setupListeners()
        setupObserver()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            playlistViewModel.getAllPlaylists()
            playlistAdapter.notifyDataSetChanged()
        }
    }

    private fun setupListeners() {
        binding.llNewPlaylist.setOnClickListener {
            val createNewPlaylistBottomSheet = CreateNewPlaylistBottomSheet {
                CoroutineScope(Dispatchers.Main).launch {
                    playlistViewModel.getAllPlaylists()
                    playlistAdapter.notifyDataSetChanged()
                }
            }
            createNewPlaylistBottomSheet.show(
                requireActivity().supportFragmentManager,
                "CreateNewPlaylistBottomSheet"
            )
        }
    }

    private fun setupObserver() {
        playlistViewModel.isNoPlaylist.observe(viewLifecycleOwner) {
            binding.rvPlaylist.visibility = if (it) View.GONE else View.VISIBLE
            binding.tvNoPlaylistFound.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun setupRecyclerView() {
        CoroutineScope(Dispatchers.Main).launch {
            playlistViewModel.getAllPlaylists()
            playlistAdapter = PlaylistAdapter(
                playlistViewModel.playlists,
                { playlistModel ->
                    val bottomSheet = PlaylistOptionBottomSheet(playlistModel) {
                        CoroutineScope(Dispatchers.Main).launch {
                            playlistViewModel.getAllPlaylists()
                            playlistAdapter.notifyDataSetChanged()
                        }
                    }
                    bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
                },
                { playlistModel ->
                    // onClickItem
                },
            )
            binding.rvPlaylist.addItemDecoration(
                VerticalSpaceItemDecoration(
                    requireActivity().resources.getDimension(
                        com.intuit.sdp.R.dimen._20sdp
                    ).toInt()
                )
            )
            binding.rvPlaylist.adapter = playlistAdapter
        }
    }
}