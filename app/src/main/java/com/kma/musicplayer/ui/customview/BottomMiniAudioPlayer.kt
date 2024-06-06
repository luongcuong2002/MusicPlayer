package com.kma.musicplayer.ui.customview

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.BottomMiniAudioPlayerBinding
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.service.PlaySongService
import com.kma.musicplayer.ui.bottomsheet.song_queue.SongQueueBottomSheet
import com.kma.musicplayer.ui.screen.core.BaseActivity
import com.kma.musicplayer.ui.screen.playsong.PlaySongActivity
import com.kma.musicplayer.utils.Constant

class BottomMiniAudioPlayer : FrameLayout {

    private var binding: BottomMiniAudioPlayerBinding =
        BottomMiniAudioPlayerBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun initView(activity: BaseActivity<*>, songService: PlaySongService) {
        val rotation: Animation = AnimationUtils.loadAnimation(activity, R.anim.rotate)
        binding.ivThumbnail.startAnimation(rotation)
        songService.playingSong.observe(activity) {
            binding.tvName.text = songService.playingSong.value?.title
            binding.tvArtist.text = songService.playingSong.value?.artist?.name
            if (songService.playingSong.value is OnlineSong) {
                Glide.with(context)
                    .load((songService.playingSong.value as OnlineSong).thumbnail)
                    .placeholder(R.drawable.default_song_thumbnail)
                    .into(binding.ivThumbnail)
            } else {
                binding.ivThumbnail.setImageResource(R.drawable.default_song_thumbnail)
            }
        }
        songService.isPlaying.observe(activity) {
            if (it) {
                binding.ivPlayPause.setImageResource(R.drawable.ic_mini_pause)
            } else {
                binding.ivPlayPause.setImageResource(R.drawable.ic_mini_play)
            }
        }
        binding.ivPrevious.setOnClickListener {
            songService.playPrevious()
        }
        binding.ivPlayPause.setOnClickListener {
            if (songService.isPlaying.value == true) {
                songService.pause()
            } else {
                songService.resume()
            }
        }
        binding.ivNext.setOnClickListener {
            songService.playNext()
        }
        binding.ivSongQueue.setOnClickListener {
            val bottomSheet = SongQueueBottomSheet(
                songs = songService.songs,
                playingSongIndex = songService.currentIndex,
                onPlayingSongIndexChanged = {
                    songService.changeCurrentIndex(it)
                }
            )
            bottomSheet.show(activity.supportFragmentManager, bottomSheet.tag)
        }
        binding.root.setOnClickListener {
            activity.showActivity(PlaySongActivity::class.java, Bundle().apply {
                putBoolean(Constant.BUNDLE_IS_FROM_BOTTOM_MINI_PLAYER, true)
            })
        }
    }
}