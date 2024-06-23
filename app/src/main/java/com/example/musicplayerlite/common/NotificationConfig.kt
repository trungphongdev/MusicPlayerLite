package com.example.musicplayerlite.common

import android.app.Notification
import android.app.Notification.MediaStyle
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaMetadata
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.musicplayerlite.MainActivity
import com.example.musicplayerlite.R
import com.example.musicplayerlite.model.Song
import com.example.musicplayerlite.utils.Utils


const val CHANNEL_ID = "MUSIC_CHANNEL_ID"
const val NOTIFY_ID = 1

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = context.getString(R.string.channel_name)
        val descriptionText = context.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        channel.description = descriptionText
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun createNotification(context: Context, song: Song): Notification {
    val notIntent = Intent(context, MainActivity::class.java)
    notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val pendingIntent = PendingIntent.getActivity(
        context, 0, notIntent,
        if (Utils.isAndroidS()) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    )
    return NotificationCompat.Builder(context, CHANNEL_ID)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setSmallIcon(R.drawable.ic_music)
        .setOngoing(true)

        .setCustomContentView(getRemoteViewsNormal(context, song))
        .setContentIntent(pendingIntent)
        .setCustomBigContentView(getRemoteViewsExpand(context, song))
        .setOnlyAlertOnce(true)
        .setAutoCancel(false)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()
}

fun getRemoteViewsExpand(context: Context, song: Song): RemoteViews {
    return RemoteViews(context.packageName, R.layout.layout_notification_large).apply {
        setImageViewUri(R.id.imvArt, song.artworkUri)
        setTextViewText(R.id.tvArtist, song.getNameArtist(context))
        setTextViewText(R.id.tvTileSong, song.title)
    }
}

fun getRemoteViewsNormal(context: Context, song: Song): RemoteViews {
    return RemoteViews(context.packageName, R.layout.layout_notification_small).apply {
        setImageViewUri(R.id.imvArt, song.artworkUri)
        setTextViewText(R.id.tvArtist, song.artist)
        setTextViewText(R.id.tvTileSong, song.getNameArtist(context))
    }
}

