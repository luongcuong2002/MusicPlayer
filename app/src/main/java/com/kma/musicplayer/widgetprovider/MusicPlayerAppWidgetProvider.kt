package com.kma.musicplayer.widgetprovider

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.kma.musicplayer.R
import com.kma.musicplayer.service.PlaySongService
import com.kma.musicplayer.service.ServiceController
import com.kma.musicplayer.ui.screen.main.MainActivity

class MusicPlayerAppWidgetProvider : AppWidgetProvider() {

    private val BUTTON_PLAY_PAUSE = "BUTTON_PLAY_PAUSE"

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        when (intent?.action) {
            BUTTON_PLAY_PAUSE -> {
                if (ServiceController.isServiceRunning(context, PlaySongService::class.java)) {
                    Toast.makeText(context, "Service is running", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Service is not running", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
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
                setOnClickPendingIntent(R.id.iv_play_pause, getPendingSelfIntent(context, BUTTON_PLAY_PAUSE))
            }

            // Tell the AppWidgetManager to perform an update on the current
            // widget.
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun getPendingSelfIntent(context: Context, action: String): PendingIntent {
        val intent = Intent(context, this.javaClass)
        intent.setAction(action)
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
}