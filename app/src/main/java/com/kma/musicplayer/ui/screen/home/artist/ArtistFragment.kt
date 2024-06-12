package com.kma.musicplayer.ui.screen.home.artist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.FragmentArtistBinding
import com.kma.musicplayer.model.Theme
import com.kma.musicplayer.ui.bottomsheet.add_to_playlist.AddToPlaylistBottomSheet
import com.kma.musicplayer.ui.bottomsheet.artist_option.ArtistOptionBottomSheet
import com.kma.musicplayer.ui.bottomsheet.create_new_playlist.CreateNewPlaylistBottomSheet
import com.kma.musicplayer.ui.bottomsheet.playlist_option.PlaylistOptionBottomSheet
import com.kma.musicplayer.ui.customview.VerticalSpaceItemDecoration
import com.kma.musicplayer.ui.screen.artistviewer.ArtistViewerActivity
import com.kma.musicplayer.ui.screen.core.BaseFragment
import com.kma.musicplayer.ui.screen.home.playlist.PlaylistAdapter
import com.kma.musicplayer.ui.screen.home.playlist.PlaylistViewModel
import com.kma.musicplayer.ui.screen.playsong.PlaySongActivity
import com.kma.musicplayer.utils.Constant
import com.kma.musicplayer.utils.SongManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

class ArtistFragment : BaseFragment<FragmentArtistBinding>() {

    private lateinit var artistViewModel: ArtistViewModel
    private var artistAdapter: ArtistAdapter? = null

    override fun getContentView(): Int = R.layout.fragment_artist

    override fun onThemeChanged(theme: Theme) {
        binding.tvNoArtistFound.setTextColor(requireActivity().getColor(theme.titleTextColorRes))
        artistAdapter?.setTheme(theme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        artistViewModel = ViewModelProvider(this)[ArtistViewModel::class.java]
        setupObserver()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            artistViewModel.getAllArtist()
            artistAdapter?.notifyDataSetChanged()
        }
    }

    private fun setupObserver() {
        artistViewModel.isNoPlaylist.observe(viewLifecycleOwner) {
            binding.rvArtist.visibility = if (it) View.GONE else View.VISIBLE
            binding.tvNoArtistFound.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun setupRecyclerView() {
        CoroutineScope(Dispatchers.Main).launch {
            artistViewModel.getAllArtist()
            artistAdapter = ArtistAdapter(
                artistViewModel.artists,
                { artist ->
                    val bottomSheet = ArtistOptionBottomSheet(
                        artist = artist,
                        onClickPlay = {
                            val songs = SongManager.getSongByArtist(artist)
                            showActivity(
                                PlaySongActivity::class.java,
                                Bundle().apply {
                                    putSerializable(Constant.BUNDLE_SONGS, songs as Serializable)
                                },
                            )
                        },
                        onClickAddToPlaylist = {
                            val songs = SongManager.getSongByArtist(artist)
                            val addToPlaylistBottomSheet =
                                AddToPlaylistBottomSheet(songs.map { it.id })
                            addToPlaylistBottomSheet.show(
                                requireActivity().supportFragmentManager,
                                addToPlaylistBottomSheet.tag
                            )
                        },
                        onClickAddToQueue = {
                            val songs = SongManager.getSongByArtist(artist)
                            getBaseActivity().songService?.addMore(songs)
                        },
                    )
                    bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
                },
                { artist ->
                    showActivity(
                        ArtistViewerActivity::class.java,
                        Bundle().apply {
                            putSerializable(Constant.BUNDLE_ARTIST, artist)
                        },
                    )
                },
            )
            binding.rvArtist.addItemDecoration(
                VerticalSpaceItemDecoration(
                    requireActivity().resources.getDimension(
                        com.intuit.sdp.R.dimen._20sdp
                    ).toInt()
                )
            )
            binding.rvArtist.adapter = artistAdapter

            getThemeViewModel().theme.observe(viewLifecycleOwner) {
                onThemeChanged(it)
            }
        }
    }
}