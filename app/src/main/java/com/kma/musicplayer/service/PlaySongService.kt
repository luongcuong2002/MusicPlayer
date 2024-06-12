package com.kma.musicplayer.service

import android.animation.ValueAnimator
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.exoplayer2.Player
import com.kma.musicplayer.R
import com.kma.musicplayer.model.RepeatMode
import com.kma.musicplayer.model.SleepTimerModel
import com.kma.musicplayer.model.Song
import com.kma.musicplayer.ui.customview.BottomMiniAudioPlayer
import com.kma.musicplayer.utils.AudioPlayerManager
import com.kma.musicplayer.utils.Constant
import com.kma.musicplayer.utils.SharePrefUtils
import com.kma.musicplayer.utils.SongManager
import com.kma.musicplayer.widgetprovider.MusicPlayerAppWidgetProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable


class PlaySongService : Service() {

    private val FOREGROUND_ID = 1338

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): PlaySongService {
            return this@PlaySongService
        }
    }

    override fun onBind(p0: Intent?): IBinder = binder

    companion object {
        const val ACTION_TOGGLE_PLAY_PAUSE = "ACTION_TOGGLE_PLAY_PAUSE"
        const val ACTION_PLAY_NEXT = "ACTION_PLAY_NEXT"
        const val ACTION_PLAY_PREVIOUS = "ACTION_PLAY_PREVIOUS"
    }

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                ACTION_TOGGLE_PLAY_PAUSE -> togglePlayPause()
                ACTION_PLAY_NEXT -> playNext()
                ACTION_PLAY_PREVIOUS -> playPrevious()
            }
        }
    }

    private val mFilter = IntentFilter().apply {
        addAction(ACTION_TOGGLE_PLAY_PAUSE)
        addAction(ACTION_PLAY_NEXT)
        addAction(ACTION_PLAY_PREVIOUS)
    }

    var songs: MutableList<Song> = mutableListOf()
    var currentIndex: Int = 0
    var playingSong = MutableLiveData<Song>()
    var isPlayRandomlyEnabled = MutableLiveData<Boolean>().apply {
        value = false
    }
    var repeatMode = MutableLiveData<RepeatMode>().apply {
        value = RepeatMode.NONE
    }
    var isPlaying = MutableLiveData<Boolean>().apply {
        value = false
    }
    var thumbnailRotation = MutableLiveData<Float>().apply {
        value = 0f
    }
    var bottomMiniAudioPlayer: BottomMiniAudioPlayer? = null

    private var _audioPlayerManager: AudioPlayerManager? = null
    val audioPlayerManager: AudioPlayerManager?
        get() = _audioPlayerManager

    val sleepTimerModel = MutableLiveData<SleepTimerModel?>(null)
    val isSleepTimerEnabled: Boolean
        get() = sleepTimerModel.value != null

    private var _job: Job? = null
    var sleepTimerRemainingTime = MutableLiveData<Long>(0)

    data class SongStatus(
        val song: Song,
        val isPlaying: Boolean
    ) : Serializable

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("PlaySongService", "onStartCommand")
        SongManager.fetchAllOnlineSong(
            onSuccess = {
                val songIds = SharePrefUtils.getSongIds()
                val currentSongIndex = SharePrefUtils.getCurrentSongIndex()
                songs.addAll(
                    songIds?.mapNotNull { id ->
                        SongManager.getSongById(id)
                    } ?: it
                )
                currentIndex = Math.max(0, currentSongIndex)

                setupThumbnailAnimation()
                _audioPlayerManager = AudioPlayerManager(this, songs)
                _audioPlayerManager?.simpleExoPlayer?.addListener(object : Player.EventListener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        super.onPlaybackStateChanged(playbackState)
                        if (playbackState == Player.STATE_ENDED) {

                            if (isSleepTimerEnabled && sleepTimerModel.value == SleepTimerModel.END_OF_TRACK) {
                                isPlaying.value = false
                                stopSleepTimer()
                                return
                            }

                            when (repeatMode.value) {
                                RepeatMode.NONE -> {
                                    if (currentIndex == songs.size - 1) {
                                        isPlaying.value = false
                                    } else {
                                        playNext()
                                    }
                                }
                                RepeatMode.REPEAT_ONE -> playAt(currentIndex)
                                RepeatMode.REPEAT_ALL -> playNext()
                                null -> TODO()
                            }
                        }
                    }
                })

                setPlayerAt(currentIndex)
                startForeground(FOREGROUND_ID, buildForegroundNotification())
                setupObservers()
            },
            onError = {
                Log.d("PlaySongService", "onError")
            }
        )
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, mFilter)
        return START_STICKY
    }

    private fun buildForegroundNotification(): Notification {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }
        val b = NotificationCompat.Builder(this, channelId)
        b.setOngoing(true)
            .setContentTitle("Music Player")
            .setContentText("Playing song")
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setSmallIcon(R.drawable.app_logo)

        return (b.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    private fun setupObservers() {
        isPlaying.observeForever {
            updateWidgetUI()
        }
        playingSong.observeForever {
            updateWidgetUI()
        }
    }

    private fun updateWidgetUI() {
        val intent = Intent(this, MusicPlayerAppWidgetProvider::class.java)
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        val ids: IntArray = AppWidgetManager.getInstance(application)
            .getAppWidgetIds(ComponentName(getApplication(), MusicPlayerAppWidgetProvider::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        intent.putExtra(Constant.BUNDLE_SONG_STATUS, SongStatus(playingSong.value!!, isPlaying.value!!))
        sendBroadcast(intent)
    }

    private fun setupThumbnailAnimation() {
        val va = ValueAnimator.ofFloat(0f, 360f)
        va.duration = 15000 //in millis
        va.addUpdateListener { animation ->
            thumbnailRotation.value = animation.animatedValue as Float
        }
        va.repeatCount = ValueAnimator.INFINITE
        va.start()
    }

    fun togglePlayPause() {
        if (isPlaying.value == true) {
            pause()
        } else {
            resume()
        }
    }

    fun pause() {
        _audioPlayerManager?.simpleExoPlayer?.pause()
        isPlaying.value = false
    }

    fun resume() {
        isPlaying.value = true
        // check if the end of the track is reached
        if (Math.abs((_audioPlayerManager?.simpleExoPlayer?.currentPosition ?: 0) - (_audioPlayerManager?.simpleExoPlayer?.duration ?: 0)) < 1000) {
            // replay the song
            playAt(currentIndex)
        } else {
            _audioPlayerManager?.simpleExoPlayer?.play()
        }
    }

    fun addMore(songs: List<Song>) {
        this.songs.addAll(songs)
    }

    fun playNext() {
        if (isPlayRandomlyEnabled.value == true) {
            val randomIndex = (0 until songs.size).random()
            updateCurrentIndexValue(randomIndex)
        } else {
            val nextIndex = if (currentIndex == songs.size - 1) 0 else currentIndex + 1
            updateCurrentIndexValue(nextIndex)
        }
        playAt(currentIndex)
    }

    fun playPrevious() {
        val index = if (currentIndex == 0) songs.size - 1 else currentIndex - 1
        updateCurrentIndexValue(index)
        playAt(currentIndex)
    }

    fun playAt(index: Int) {
        updateCurrentIndexValue(index)
        _audioPlayerManager?.play(currentIndex)
        isPlaying.value = true
    }

    fun setPlayerAt(index: Int) {
        updateCurrentIndexValue(index)
        _audioPlayerManager?.setPlayerAt(index)
        isPlaying.value = false
    }

    fun setPlayRandomlyEnabled(isEnabled: Boolean) {
        isPlayRandomlyEnabled.value = isEnabled
    }

    fun setRepeatMode(mode: RepeatMode) {
        repeatMode.value = mode
    }

    fun changeCurrentIndex(index: Int) {
        currentIndex = index
    }

    fun setSleepTimerModel(model: SleepTimerModel?, timeInMillis: Long) {
        sleepTimerModel.value = model
        // if model is null, cancel the sleep timer
        if (model == null) {
            stopSleepTimer()
        } else {
            if (model == SleepTimerModel.END_OF_TRACK) {
                _job?.cancel()
                sleepTimerRemainingTime.value = 0
            } else {
                startSleepTimer(timeInMillis)
            }
        }
    }

    private fun startSleepTimer(timeInMillis: Long) {
        _job?.cancel()
        _job = CoroutineScope(Dispatchers.Main).launch {
            sleepTimerRemainingTime.value = timeInMillis
            while (sleepTimerRemainingTime.value!! > 0) {
                delay(1000)
                sleepTimerRemainingTime.value = sleepTimerRemainingTime.value!! - 1
            }
            stopSleepTimer()
            pause()
        }
    }

    private fun stopSleepTimer() {
        _job?.cancel()
        sleepTimerModel.value = null
        sleepTimerRemainingTime.value = 0
    }

    private fun updateCurrentIndexValue(index: Int) {
        currentIndex = index
        playingSong.value = songs[currentIndex]
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d("PlaySongService", "onTaskRemoved")
        SharePrefUtils.saveSongIds(songs.map { it.id })
        SharePrefUtils.saveCurrentSongIndex(currentIndex)
    }

    override fun onDestroy() {
        super.onDestroy()
        _audioPlayerManager?.releasePlayer()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver)
    }
}