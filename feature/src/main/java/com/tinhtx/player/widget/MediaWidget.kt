package com.tinhtx.player.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.tinhtx.player.feature.R

class MediaWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {
        private const val ACTION_PLAY_PAUSE = "com.tinhtx.player.ACTION_PLAY_PAUSE"
        private const val ACTION_NEXT = "com.tinhtx.player.ACTION_NEXT"
        private const val ACTION_PREVIOUS = "com.tinhtx.player.ACTION_PREVIOUS"

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.media_widget)

            // Set up click listeners for control buttons
            setupButtonClickListeners(context, views)

            // Update widget content
            updateWidgetContent(views, "No media playing", "")

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun setupButtonClickListeners(context: Context, views: RemoteViews) {
            // Play/Pause button
            val playPauseIntent = Intent(context, MediaWidget::class.java).apply {
                action = ACTION_PLAY_PAUSE
            }
            val playPausePendingIntent = PendingIntent.getBroadcast(
                context, 0, playPauseIntent, PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_play_pause, playPausePendingIntent)

            // Next button
            val nextIntent = Intent(context, MediaWidget::class.java).apply {
                action = ACTION_NEXT
            }
            val nextPendingIntent = PendingIntent.getBroadcast(
                context, 1, nextIntent, PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_next, nextPendingIntent)

            // Previous button
            val previousIntent = Intent(context, MediaWidget::class.java).apply {
                action = ACTION_PREVIOUS
            }
            val previousPendingIntent = PendingIntent.getBroadcast(
                context, 2, previousIntent, PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_previous, previousPendingIntent)
        }

        private fun updateWidgetContent(views: RemoteViews, title: String, artist: String) {
            views.setTextViewText(R.id.widget_title, title)
            views.setTextViewText(R.id.widget_artist, artist)
        }

        fun updateAllWidgets(context: Context, title: String, artist: String, isPlaying: Boolean) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, MediaWidget::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)

            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.media_widget)
                setupButtonClickListeners(context, views)
                updateWidgetContent(views, title, artist)

                // Update play/pause button icon
                val playPauseIcon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                views.setImageViewResource(R.id.widget_play_pause, playPauseIcon)

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            ACTION_PLAY_PAUSE -> {
                // Handle play/pause action
                // This would typically send a broadcast to the media service
            }
            ACTION_NEXT -> {
                // Handle next action
            }
            ACTION_PREVIOUS -> {
                // Handle previous action
            }
        }
    }
}
