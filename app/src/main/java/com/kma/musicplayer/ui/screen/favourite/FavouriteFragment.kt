package com.kma.musicplayer.ui.screen.favourite

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.kma.musicplayer.databinding.FragmentFavouriteBinding
import com.kma.musicplayer.ui.screen.core.BaseFragment
import com.kma.musicplayer.R
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.model.Theme
import com.kma.musicplayer.ui.bottomsheet.add_to_playlist.AddToPlaylistBottomSheet
import com.kma.musicplayer.ui.bottomsheet.song_option.SongOptionBottomSheet
import com.kma.musicplayer.ui.screen.home.song.SongAdapter
import com.kma.musicplayer.ui.screen.playsong.PlaySongActivity
import com.kma.musicplayer.utils.Constant
import com.kma.musicplayer.utils.ShareUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>() {

    private lateinit var favouriteViewModel: FavouriteViewModel
    private var songAdapter: SongAdapter? = null

    override fun getContentView(): Int = R.layout.fragment_favourite

    override fun onThemeChanged(theme: Theme) {
        binding.tvTitle.setTextColor(requireActivity().getColor(theme.titleTextColorRes))
        binding.tvMyFavouriteSongs.setTextColor(requireActivity().getColor(theme.titleTextColorRes))
        binding.tvSongs.setTextColor(requireActivity().getColor(theme.titleTextColorRes))
        binding.tvPlay.setTextColor(requireActivity().getColor(theme.titleTextColorRes))
        binding.llPlay.setBackgroundResource(theme.backgroundPlayButtonRes)
        binding.tvNoSongs.setTextColor(requireActivity().getColor(theme.titleTextColorRes))
        songAdapter?.setTheme(theme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouriteViewModel =
            ViewModelProvider(requireActivity()).get(FavouriteViewModel::class.java)
        setupListeners()
        setupObserver()
        setupRecyclerView()
    }

    private fun setupListeners() {
        binding.llShuffle.setOnClickListener {
            if (favouriteViewModel.songs.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_song_to_play),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val cloneSong = favouriteViewModel.songs.map { it }.shuffled()
            showActivity(
                PlaySongActivity::class.java,
                Bundle().apply {
                    putSerializable(Constant.BUNDLE_SONGS, cloneSong as Serializable)
                },
            )
        }
        binding.llPlay.setOnClickListener {
            if (favouriteViewModel.songs.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_song_to_play),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            showActivity(
                PlaySongActivity::class.java,
                Bundle().apply {
                    putSerializable(Constant.BUNDLE_SONGS, favouriteViewModel.songs as Serializable)
                },
            )
        }
    }

    private fun setupObserver() {
        favouriteViewModel.size.observe(viewLifecycleOwner) {
            binding.tvTotalSongs.text = if (it > 1) {
                "${it} ${getString(R.string.songs)}"
            } else {
                "${it} ${getString(R.string.song)}"
            }
            if (it == 0) {
                binding.rvFavourite.visibility = View.GONE
                binding.tvNoSongs.visibility = View.VISIBLE
            } else {
                binding.rvFavourite.visibility = View.VISIBLE
                binding.tvNoSongs.visibility = View.GONE
            }
        }
    }

    private fun setupRecyclerView() {
        CoroutineScope(Dispatchers.Main).launch {
            favouriteViewModel.fetchFavouriteSongs()
            songAdapter = SongAdapter(
                songs = favouriteViewModel.songs,
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
                                favouriteViewModel.filterHiddenSongs()
                                songAdapter?.notifyDataSetChanged()
                                bottomSheet?.dismiss()
                            }
                        },
                        onClickShare = {
                            ShareUtils.shareSong(requireActivity(), song)
                            bottomSheet?.dismiss()
                        },
                        onChangeFavorite = {
                            CoroutineScope(Dispatchers.Main).launch {
                                favouriteViewModel.filterFavoriteSongs()
                                songAdapter?.notifyDataSetChanged()
                                bottomSheet?.dismiss()
                            }
                        },
                    )
                    bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
                },
                onSongClick = { song ->
                    showActivity(
                        PlaySongActivity::class.java,
                        Bundle().apply {
                            putSerializable(
                                Constant.BUNDLE_SONGS,
                                mutableListOf(song) as Serializable
                            )
                        },
                    )
                },
            )
            binding.rvFavourite.adapter = songAdapter

            getThemeViewModel().theme.observe(viewLifecycleOwner) {
                onThemeChanged(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        songAdapter?.let {
            CoroutineScope(Dispatchers.Main).launch {
                favouriteViewModel.filterFavoriteSongs()
                it.notifyDataSetChanged()
            }
        }
    }
}