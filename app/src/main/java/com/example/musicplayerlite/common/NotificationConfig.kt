package com.example.musicplayerlite.common

import android.app.Notification
import android.app.Notification.MediaStyle
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaMetadata
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaStyleNotificationHelper
import androidx.media3.ui.PlayerNotificationManager.ACTION_PLAY
import com.bumptech.glide.Glide
import com.example.musicplayerlite.App
import com.example.musicplayerlite.MainActivity
import com.example.musicplayerlite.R
import com.example.musicplayerlite.model.Song
import com.example.musicplayerlite.receiver.MusicReceiver
import com.example.musicplayerlite.utils.Utils


object NotificationConfig{
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

    @OptIn(UnstableApi::class)
    fun createNotification(context: Context, song: Song): Notification {
        val notIntent = Intent(context, MainActivity::class.java)
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, notIntent,
            getFlagsPendingIntent()
        )
        // Actions
        val actionPlay = NotificationCompat.Action(
            R.drawable.ic_play,
            context.getString(R.string.action_play),
            PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, MusicReceiver::class.java).setAction(context.getString(R.string.action_play)).setPackage(context.packageName),
                getFlagsPendingIntent()
            )
        )
        val actionPause = NotificationCompat.Action(R.drawable.ic_pause,
            context.getString(R.string.action_pause),
            PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, MusicReceiver::class.java).setAction(context.getString(R.string.action_pause)).setPackage(context.packageName),
                getFlagsPendingIntent()
            )
        )
        val actionNext = NotificationCompat.Action(
            R.drawable.ic_next,
            context.getString(R.string.action_next),
            PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, MusicReceiver::class.java).setAction(context.getString(R.string.action_next)).setPackage(context.packageName),
                getFlagsPendingIntent()
            )
        )
        val actionPrevious = NotificationCompat.Action(
            R.drawable.ic_previous,
            context.getString(R.string.action_previous),
            PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, MusicReceiver::class.java).setAction(context.getString(R.string.action_previous)).setPackage(context.packageName),
                getFlagsPendingIntent()
            )
        )
        val player = ExoPlayer.Builder(context).build()
        val artwork = Glide.with(context).asBitmap().load(R.drawable.img_ob_3).submit().get()

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_music)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setStyle(
                MediaStyleNotificationHelper.MediaStyle(MediaSession.Builder(context, player).build())
                    .setShowActionsInCompactView(0,1,2)
            )
            .setLargeIcon(artwork)
            .setOnlyAlertOnce(true)
            .setAutoCancel(false)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(actionPrevious)
            .addAction(actionPlay)
            .addAction(actionNext)
            .build()
    }
    private fun getFlagsPendingIntent(): Int = if (Utils.isAndroidS()) {
        PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
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


}
