package com.kma.musicplayer.ui.screen.home.playlist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.FragmentPlaylistBinding
import com.kma.musicplayer.model.Theme
import com.kma.musicplayer.ui.bottomsheet.create_new_playlist.CreateNewPlaylistBottomSheet
import com.kma.musicplayer.ui.bottomsheet.playlist_option.PlaylistOptionBottomSheet
import com.kma.musicplayer.ui.customview.VerticalSpaceItemDecoration
import com.kma.musicplayer.ui.screen.core.BaseFragment
import com.kma.musicplayer.ui.screen.playlistviewer.PlaylistViewerActivity
import com.kma.musicplayer.ui.screen.playsong.PlaySongActivity
import com.kma.musicplayer.utils.Constant
import com.kma.musicplayer.utils.SongManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

class PlaylistFragment : BaseFragment<FragmentPlaylistBinding>() {

    private lateinit var playlistViewModel: PlaylistViewModel
    private var playlistAdapter: PlaylistAdapter? = null

    override fun getContentView(): Int = R.layout.fragment_playlist

    override fun onThemeChanged(theme: Theme) {
        binding.tvPlaylists.setTextColor(requireContext().getColor(theme.adapterTitleTextColorRes))
        binding.ivNewPlaylist.setImageResource(theme.imagePlusCircle)
        binding.tvNewPlaylist.setTextColor(requireContext().getColor(theme.adapterSubTitleTextColorRes))
        binding.tvNoPlaylistFound.setTextColor(requireContext().getColor(theme.titleTextColorRes))
        playlistAdapter?.setTheme(theme)
    }

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
            playlistAdapter?.notifyDataSetChanged()
        }
    }

    private fun setupListeners() {
        binding.llNewPlaylist.setOnClickListener {
            val createNewPlaylistBottomSheet = CreateNewPlaylistBottomSheet {
                CoroutineScope(Dispatchers.Main).launch {
                    playlistViewModel.getAllPlaylists()
                    playlistAdapter?.notifyDataSetChanged()
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
                    val bottomSheet = PlaylistOptionBottomSheet(
                        playlistModel = playlistModel,
                        onPlaylistDeleted = {
                            CoroutineScope(Dispatchers.Main).launch {
                                playlistViewModel.getAllPlaylists()
                                playlistAdapter?.notifyDataSetChanged()
                            }
                        },
                        onClickPlay = {
                            val songs = playlistModel.songIds.map {
                                SongManager.getSongById(it)
                            }.filterNotNull()
                            if (songs.isEmpty()) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.no_song_in_playlist),
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@PlaylistOptionBottomSheet
                            }
                            showActivity(
                                PlaySongActivity::class.java,
                                Bundle().apply {
                                    putSerializable(Constant.BUNDLE_SONGS, songs as Serializable)
                                },
                            )
                        },
                        onClickAddToQueue = {
                            val songs = playlistModel.songIds.map {
                                SongManager.getSongById(it)
                            }.filterNotNull()
                            getBaseActivity().songService?.addMore(songs)
                        },
                    )
                    bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
                },
                { playlistModel ->
                    if (playlistModel.songIds.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.no_song_in_playlist),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@PlaylistAdapter
                    }
                    showActivity(PlaylistViewerActivity::class.java, Bundle().apply {
                        putSerializable(Constant.BUNDLE_PLAYLIST, playlistModel)
                    })
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

            getThemeViewModel().theme.observe(viewLifecycleOwner) {
                onThemeChanged(it)
            }
        }
    }
}