package com.fadlurahmanf.starterappmvvm.unknown.utils.media

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.data.constant.logConsole

class MediaPlayerNotificationHelper(
    var context: Context
) {
    companion object{
        const val MEDIA_PLAYER_CHANNEL_ID = "MEDIA_PLAYER_CHANNEL_ID"
        const val MEDIA_PLAYER_CHANNEL = "MEDIA_PLAYER_CHANNEL"
        const val MEDIA_PLAYER_CHANNEL_DESCRIPTION = "Media Player Notification"

        const val STATE_PLAYING = "STATE_PLAYING"
        const val STATE_PAUSED = "STATE_PAUSED"

        const val EXTRA_STATE = "EXTRA_STATE"
        const val EXTRA_DURATION_AUDIO = "EXTRA_DURATION_AUDIO"
        const val EXTRA_CURRENT_POSITION = "EXTRA_CURRENT_POSITION"
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

    private fun getBasedNotification(data: Bundle): NotificationCompat.Builder {
        val isPlay = (data.getString(EXTRA_STATE) == STATE_PLAYING)
        val duration:Int = data.getInt(EXTRA_DURATION_AUDIO, 0)
        val currentPosition = data.getInt(EXTRA_CURRENT_POSITION, 0)

        val mediaSession = MediaSessionCompat(context, "MediaPlayerService")
        mediaSession.setPlaybackState(PlaybackStateCompat.Builder()
                .setState(if (isPlay) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED, currentPosition.toLong(), 1f)
                .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                .build())
        mediaSession.setMetadata(MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "Title")
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "Artist")
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration.toLong())
                .build())
        mediaSession.setCallback(object : MediaSessionCompat.Callback() {
            override fun onSeekTo(pos: Long) {
                super.onSeekTo(pos)
                logConsole.d("audioSeekTo: $pos")
                val intent = MediaPlayerReceiver.getSeekToIntent(context, pos)
                context.sendBroadcast(intent)
            }
        })
        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle()
            .setMediaSession(mediaSession.sessionToken)
            .setShowActionsInCompactView(0, 1, 2)
        return NotificationCompat.Builder(
            context,
            MEDIA_PLAYER_CHANNEL_ID
        ).setSmallIcon(R.drawable.il_logo_bankmas)
            .setStyle(mediaStyle)
    }

    fun getPlayedMediaNotification(data:Bundle): Notification {
        return getBasedNotification(data)
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

    fun getPausedNotification(data: Bundle): Notification {
        return getBasedNotification(data)
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
        return PendingIntent.getBroadcast(context, 0,
            MediaPlayerReceiver.getStopIntent(context), getFlagPendingIntent())
    }

    private fun getPausePendingIntent():PendingIntent{
        return PendingIntent.getBroadcast(context, 1,
            MediaPlayerReceiver.getPauseIntent(context), getFlagPendingIntent())
    }

    private fun getResumePendingIntent():PendingIntent{
        return PendingIntent.getBroadcast(context, 2,
            MediaPlayerReceiver.getResume(context), getFlagPendingIntent())
    }

    private fun getFlagPendingIntent(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    }
}