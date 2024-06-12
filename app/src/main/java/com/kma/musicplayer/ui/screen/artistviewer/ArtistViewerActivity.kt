package com.kma.musicplayer.ui.screen.artistviewer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.kma.musicplayer.databinding.ActivityArtistViewerBinding
import com.kma.musicplayer.ui.screen.core.BaseActivity
import com.kma.musicplayer.R
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.model.Artist
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

class ArtistViewerActivity : BaseActivity<ActivityArtistViewerBinding>() {
    private lateinit var artistViewerViewModel: ArtistViewerViewModel
    private var songAdapter: SongAdapter? = null

    override fun getContentView(): Int = R.layout.activity_artist_viewer

    override fun onThemeChanged(theme: Theme) {
        binding.root.setBackgroundColor(getColor(theme.backgroundColorRes))
        binding.tvArtistName.setTextColor(getColor(theme.titleTextColorRes))
        binding.backButton.setImageResource(theme.imageBackButtonRes)
        binding.tvSongs.setTextColor(getColor(theme.titleTextColorRes))
        binding.tvPlay.setTextColor(getColor(theme.titleTextColorRes))
        binding.llPlay.setBackgroundResource(theme.backgroundPlayButtonRes)
        binding.tvNoSongs.setTextColor(getColor(theme.titleTextColorRes))
        songAdapter?.setTheme(theme)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        artistViewerViewModel =
            ViewModelProvider(this).get(ArtistViewerViewModel::class.java)
        val artist = intent.getSerializableExtra(Constant.BUNDLE_ARTIST) as Artist
        artistViewerViewModel.fetchSongs(artist)
        initView()
        setupListeners()
        setupObserver()
        setupRecyclerView()
    }

    private fun initView() {
        val artist = intent.getSerializableExtra(Constant.BUNDLE_ARTIST) as Artist
        binding.tvArtistName.text = artist.name
        Glide.with(this)
            .load(artist.thumbnail)
            .into(binding.ivThumbnail)
    }

    private fun setupListeners() {
        binding.llShuffle.setOnClickListener {
            if (artistViewerViewModel.songs.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.no_song_to_play),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val cloneSong = artistViewerViewModel.songs.map { it }.shuffled()
            showActivity(
                PlaySongActivity::class.java,
                Bundle().apply {
                    putSerializable(Constant.BUNDLE_SONGS, cloneSong as Serializable)
                },
            )
        }
        binding.llPlay.setOnClickListener {
            if (artistViewerViewModel.songs.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.no_song_to_play),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            showActivity(
                PlaySongActivity::class.java,
                Bundle().apply {
                    putSerializable(Constant.BUNDLE_SONGS, artistViewerViewModel.songs as Serializable)
                },
            )
        }
    }

    private fun setupObserver() {
        artistViewerViewModel.size.observe(this) {
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
        songAdapter = SongAdapter(
            songs = artistViewerViewModel.songs,
            onMoreClick = { song ->
                var bottomSheet: SongOptionBottomSheet? = null
                bottomSheet = SongOptionBottomSheet(
                    song = song,
                    onClickPlayNext = {
                        songService?.songs?.add(song)
                        bottomSheet?.dismiss()
                    },
                    onClickAddToPlaylist = {
                        val addToPlaylistBottomSheet = AddToPlaylistBottomSheet(listOf(song.id))
                        addToPlaylistBottomSheet.show(
                            supportFragmentManager,
                            addToPlaylistBottomSheet.tag
                        )
                        bottomSheet?.dismiss()
                    },
                    onClickHide = {
                        CoroutineScope(Dispatchers.Main).launch {
                            AppDatabase.INSTANCE.hiddenSongDao().insert(song.id)
                            artistViewerViewModel.filterHiddenSongs()
                            songAdapter?.notifyDataSetChanged()
                            bottomSheet?.dismiss()
                        }
                    },
                    onClickShare = {
                        ShareUtils.shareSong(this@ArtistViewerActivity, song)
                        bottomSheet?.dismiss()
                    },
                )
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
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
    }
}