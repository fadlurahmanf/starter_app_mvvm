package com.fadlurahmanf.starterappmvvm.utils.media

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import com.fadlurahmanf.starterappmvvm.R

class MediaPlayerNotificationHelper(
    var context: Context
) {
    companion object{
        const val MEDIA_PLAYER_CHANNEL_ID = "MEDIA_PLAYER_CHANNEL_ID"
        const val MEDIA_PLAYER_CHANNEL = "MEDIA_PLAYER_CHANNEL"
        const val MEDIA_PLAYER_CHANNEL_DESCRIPTION = "Media Player Notification"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(MEDIA_PLAYER_CHANNEL_ID, MEDIA_PLAYER_CHANNEL, importance).apply {
                description = MEDIA_PLAYER_CHANNEL_DESCRIPTION
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getBasedNotification(): NotificationCompat.Builder {
        val mediaSession = MediaSessionCompat(context, "MediaPlayerService")
        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle()
            .setMediaSession(mediaSession.sessionToken)
            .setShowActionsInCompactView(0, 1, 2)
        return NotificationCompat.Builder(
            context,
            MEDIA_PLAYER_CHANNEL_ID
        ).setSmallIcon(R.drawable.il_logo_bankmas)
            .setContentTitle("Track - Title")
            .setContentText("Artist - Album")
            .setStyle(mediaStyle)
    }

    fun getPlayedMediaNotification(): Notification {
        return getBasedNotification()
            .addAction(NotificationCompat.Action(
                R.drawable.ic_round_skip_previous,
                "Previous",
                MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_PAUSE)
            ))
            .addAction(NotificationCompat.Action(
                R.drawable.ic_round_pause,
                "Pause",
                getPausePendingIntent()
            ))
            .addAction(NotificationCompat.Action(
                R.drawable.ic_round_skip_next,
                "Next",
                MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_PAUSE)
            ))
            .setOngoing(true)
            .build()
    }

    fun getPausedNotification(): Notification {
        return getBasedNotification()
            .addAction(NotificationCompat.Action(
                R.drawable.ic_round_skip_previous,
                "Previous",
                MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_PAUSE)
            ))
            .addAction(NotificationCompat.Action(
                R.drawable.ic_round_play_arrow,
                "Play",
                getResumePendingIntent()
            ))
            .addAction(NotificationCompat.Action(
                R.drawable.ic_round_skip_next,
                "Next",
                MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_PAUSE)
            ))
            .setOngoing(false)
            .build()
    }

    private fun getDeletePendingIntent():PendingIntent{
        return PendingIntent.getBroadcast(context, 0, MediaPlayerReceiver.getStopIntent(context), getFlagPendingIntent())
    }

    private fun getPausePendingIntent():PendingIntent{
        return PendingIntent.getBroadcast(context, 1, MediaPlayerReceiver.getPauseIntent(context), getFlagPendingIntent())
    }

    private fun getResumePendingIntent():PendingIntent{
        return PendingIntent.getBroadcast(context, 2, MediaPlayerReceiver.getResume(context), getFlagPendingIntent())
    }

    private fun getFlagPendingIntent(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    }
}