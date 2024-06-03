package com.kma.musicplayer.ui.screen.playsong

import android.content.ComponentName
import android.os.Bundle
import android.os.IBinder
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.kma.musicplayer.R
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.databinding.ActivityPlaySongBinding
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.model.RepeatMode
import com.kma.musicplayer.model.Song
import com.kma.musicplayer.model.nextMode
import com.kma.musicplayer.ui.bottomsheet.song_queue.SongQueueBottomSheet
import com.kma.musicplayer.ui.screen.core.BaseActivity
import com.kma.musicplayer.utils.Constant
import com.kma.musicplayer.utils.ShareUtils


class PlaySongActivity : BaseActivity<ActivityPlaySongBinding>() {
    override fun getContentView(): Int = R.layout.activity_play_song

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rotation: Animation = AnimationUtils.loadAnimation(this, R.anim.rotate)
        binding.ivThumbnail.startAnimation(rotation)
    }

    override fun onServiceConnected(className: ComponentName, service: IBinder) {
        super.onServiceConnected(className, service)
        setupPlayers()
        setupListeners()
        setupObservers()
    }

    private fun setupPlayers() {
        val songs = intent.getSerializableExtra(Constant.BUNDLE_SONGS)
        val currentIndex = intent.getIntExtra(Constant.BUNDLE_START_FROM_INDEX, 0)
        binding.playerView.player = songService?.audioPlayerManager?.simpleExoPlayer
        songService?.setSongs(songs as MutableList<Song>)

        setupObservers()
        setupListeners()

        songService?.playAt(currentIndex)
    }

    private fun setupListeners() {
        binding.ivFavourite.setOnClickListener {
            val song = songService?.playingSong?.value ?: return@setOnClickListener
            if (AppDatabase.INSTANCE.favouriteSongDao().isFavourite(song.id)) {
                AppDatabase.INSTANCE.favouriteSongDao().delete(song.id)
                binding.ivFavourite.setImageResource(R.drawable.ic_white_heart)
            } else {
                AppDatabase.INSTANCE.favouriteSongDao().insert(song.id)
                binding.ivFavourite.setImageResource(R.drawable.ic_purple_heart)
            }
        }
        binding.ivShare.setOnClickListener {
            val song = songService?.playingSong?.value ?: return@setOnClickListener
            ShareUtils.shareSong(this, song)
        }
        binding.llTimer.setOnClickListener {

        }
        binding.llRandom.setOnClickListener {
            songService?.setPlayRandomlyEnabled(
                !(songService?.isPlayRandomlyEnabled?.value ?: false)
            )
        }
        binding.llRepeat.setOnClickListener {
            songService?.setRepeatMode(
                songService?.repeatMode?.value?.nextMode() ?: RepeatMode.NONE
            )
        }
        binding.llQueue.setOnClickListener {
            songService?.let {
                val bottomSheet = SongQueueBottomSheet(
                    songs = it.songs,
                    playingSongIndex = it.currentIndex
                ) {
                }
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            }
        }
    }

    private fun setupObservers() {
        songService?.isPlayRandomlyEnabled?.observe(this) {
            binding.ivRandom.setImageResource(if (it) R.drawable.ic_random_on else R.drawable.ic_random_off)
        }
        songService?.repeatMode?.observe(this) {
            when (it) {
                RepeatMode.NONE -> {
                    binding.ivRepeat.setImageResource(R.drawable.ic_no_repeat)
                }

                RepeatMode.REPEAT_ALL -> {
                    binding.ivRepeat.setImageResource(R.drawable.ic_repeat_forever)
                }

                RepeatMode.REPEAT_ONE -> {
                    binding.ivRepeat.setImageResource(R.drawable.ic_repeat_one)
                }
            }
        }
        songService?.isAbleToNext?.observe(this) {
            val ivNext = findViewById<ImageView>(R.id.iv_next) ?: return@observe
            if (it) {
                ivNext.alpha = 1f
                ivNext.isClickable = true
            } else {
                ivNext.alpha = 0.5f
                ivNext.isClickable = false
            }
        }
        songService?.isAbleToPrevious?.observe(this) {
            val ivPrevious = findViewById<ImageView>(R.id.iv_previous) ?: return@observe
            if (it) {
                ivPrevious.alpha = 1f
                ivPrevious.isClickable = true
            } else {
                ivPrevious.alpha = 0.5f
                ivPrevious.isClickable = false
            }
        }
        songService?.playingSong?.observe(this) {
            updateUIBasedOnCurrentSong()
        }
    }

    private fun updateUIBasedOnCurrentSong() {
        songService?.playingSong?.value?.let {
            binding.tvSongName.text = it.title
            binding.tvArtist.text = it.artist
            if (AppDatabase.INSTANCE.favouriteSongDao().isFavourite(it.id)) {
                binding.ivFavourite.setImageResource(R.drawable.ic_purple_heart)
            } else {
                binding.ivFavourite.setImageResource(R.drawable.ic_white_heart)
            }
            if (it is OnlineSong) {
                Glide.with(this)
                    .load(it.thumbnail)
                    .placeholder(R.drawable.default_song_thumbnail)
                    .into(binding.ivThumbnail)
            }
        }
    }
}