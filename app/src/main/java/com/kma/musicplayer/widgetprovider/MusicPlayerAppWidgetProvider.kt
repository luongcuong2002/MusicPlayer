package com.kma.musicplayer.widgetprovider

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.kma.musicplayer.R
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.model.Song
import com.kma.musicplayer.service.PlaySongService
import com.kma.musicplayer.service.ServiceController
import com.kma.musicplayer.ui.screen.main.MainActivity
import com.kma.musicplayer.utils.Constant

class MusicPlayerAppWidgetProvider : AppWidgetProvider() {

    companion object {
        private const val BUTTON_PLAY_PAUSE = "BUTTON_PLAY_PAUSE"
        private const val BUTTON_NEXT = "BUTTON_NEXT"
        private const val BUTTON_PREVIOUS = "BUTTON_PREVIOUS"
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            BUTTON_PLAY_PAUSE -> {
                LocalBroadcastManager.getInstance(context).sendBroadcast(
                    Intent(PlaySongService.ACTION_TOGGLE_PLAY_PAUSE)
                )
            }
            BUTTON_NEXT -> {
                LocalBroadcastManager.getInstance(context).sendBroadcast(
                    Intent(PlaySongService.ACTION_PLAY_NEXT)
                )
            }
            BUTTON_PREVIOUS -> {
                LocalBroadcastManager.getInstance(context).sendBroadcast(
                    Intent(PlaySongService.ACTION_PLAY_PREVIOUS)
                )
            }
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(
                    ComponentName(context, MusicPlayerAppWidgetProvider::class.java)
                )
                updateWidget(context, intent, appWidgetManager, appWidgetIds)
            }
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d("PlaySongService", "onEnabled")
        if (!ServiceController.isServiceRunning(context, PlaySongService::class.java)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(Intent(context, PlaySongService::class.java))
            } else {
                context.startService(Intent(context, PlaySongService::class.java))
            }
        } else {
            LocalBroadcastManager.getInstance(context).sendBroadcast(
                Intent(PlaySongService.ACTION_REQUEST_UPDATE_WIDGET_UI)
            )
        }
    }

    private fun updateWidget(
        context: Context,
        intent: Intent,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val songStatus = intent.getSerializableExtra(Constant.BUNDLE_SONG_STATUS) as PlaySongService.SongStatus?
        songStatus?.let {
            Log.d("PlaySongService", "updateWidget: ${it.song.title} - ${it.isPlaying}")
            appWidgetIds.forEach { appWidgetId ->
                val pendingIntent: PendingIntent = PendingIntent.getActivity(
                    /* context = */ context,
                    /* requestCode = */  0,
                    /* intent = */ Intent(context, MainActivity::class.java),
                    /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val views: RemoteViews = RemoteViews(
                    context.packageName,
                    R.layout.layout_widget_provider
                ).apply {
                    setOnClickPendingIntent(R.id.ll_widget, pendingIntent)
                    setOnClickPendingIntent(
                        R.id.iv_play_pause,
                        getPendingSelfIntent(context, BUTTON_PLAY_PAUSE)
                    )
                    setOnClickPendingIntent(
                        R.id.iv_next,
                        getPendingSelfIntent(context, BUTTON_NEXT)
                    )
                    setOnClickPendingIntent(
                        R.id.iv_previous,
                        getPendingSelfIntent(context, BUTTON_PREVIOUS)
                    )
                    setTextViewText(R.id.tv_song_name, it.song.title)
                    setTextViewText(R.id.tv_artist_name, it.song.artist.name)
                    setImageViewResource(
                        R.id.iv_play_pause,
                        if (it.isPlaying) R.drawable.ic_mini_pause else R.drawable.ic_mini_play)
                }
                if (it.song is OnlineSong) {
                    Glide.with(context).asBitmap()
                        .load(it.song.thumbnail)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                views.setImageViewBitmap(R.id.iv_thumbnail, resource)
                                appWidgetManager.updateAppWidget(appWidgetId, views)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                views.setImageViewResource(
                                    R.id.iv_thumbnail,
                                    R.drawable.default_song_thumbnail
                                )
                            }
                        })
                } else {
                    views.setImageViewResource(R.id.iv_thumbnail, R.drawable.default_song_thumbnail)
                }
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }

    private fun getPendingSelfIntent(context: Context, action: String): PendingIntent {
        val intent = Intent(context, this.javaClass)
        intent.setAction(action)
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}