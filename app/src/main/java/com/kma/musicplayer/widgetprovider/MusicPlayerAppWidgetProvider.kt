package com.kma.musicplayer.widgetprovider

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import com.kma.musicplayer.R
import com.kma.musicplayer.ui.screen.main.MainActivity
import com.kma.musicplayer.ui.screen.splash.SplashActivity

class MusicPlayerAppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.d("MusicPlayerAppWidget", "onUpdate")
        appWidgetIds.forEach { appWidgetId ->
            // Create an Intent to launch ExampleActivity.
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                /* context = */ context,
                /* requestCode = */  0,
                /* intent = */ Intent(context, MainActivity::class.java),
                /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Get the layout for the widget and attach an onClick listener to
            // the button.
            val views: RemoteViews = RemoteViews(
                context.packageName,
                R.layout.layout_widget_provider
            ).apply {
                setOnClickPendingIntent(R.id.ll_widget, pendingIntent)

            }

            // Tell the AppWidgetManager to perform an update on the current
            // widget.
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.d("MusicPlayerAppWidget", "onReceive: ${intent?.action}")
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        Log.d("MusicPlayerAppWidget", "onDisabled")
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        Log.d("MusicPlayerAppWidget", "onEnabled")
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        Log.d("MusicPlayerAppWidget", "onDeleted")
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
        Log.d("MusicPlayerAppWidget", "onRestored")
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        Log.d("MusicPlayerAppWidget", "onAppWidgetOptionsChanged")
    }
}