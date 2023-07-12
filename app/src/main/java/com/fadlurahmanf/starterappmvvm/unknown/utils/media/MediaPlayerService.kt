package com.fadlurahmanf.starterappmvvm.unknown.utils.media

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.fadlurahmanf.starterappmvvm.feature.notification.data.constant.NotificationConstant
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole

class MediaPlayerService:Service() {
    companion object{
        const val AUDIO_URL = "AUDIO_URL"
        const val NOTIFICATION_ID = "NOTIFICATION_ID"
        const val ACTION = "ACTION"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_RESUME = "ACTION_RESUME"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_SEEK_TO = "ACTION_SEEK_TO"
        const val SEEK_TO_POSITION = "SEEK_TO_POSITION"
        fun playAudio(context: Context, url:String){
            val intent = Intent(context, MediaPlayerService::class.java)
            intent.apply {
                putExtra(NOTIFICATION_ID, NotificationConstant.PLAYING_MUSIC_NOTIFICATION_ID)
                putExtra(ACTION, ACTION_PLAY)
                putExtra(AUDIO_URL, url)
            }
            ContextCompat.startForegroundService(context, intent)
        }

        fun pauseAudio(context: Context){
            val intent = Intent(context, MediaPlayerService::class.java)
            intent.apply {
                putExtra(NOTIFICATION_ID, NotificationConstant.PLAYING_MUSIC_NOTIFICATION_ID)
                putExtra(ACTION, ACTION_PAUSE)
            }
            ContextCompat.startForegroundService(context, intent)
        }

        fun resumeAudio(context: Context){
            val intent = Intent(context, MediaPlayerService::class.java)
            intent.apply {
                putExtra(NOTIFICATION_ID, NotificationConstant.PLAYING_MUSIC_NOTIFICATION_ID)
                putExtra(ACTION, ACTION_RESUME)
            }
            ContextCompat.startForegroundService(context, intent)
        }

        fun stopAudio(context: Context){
            val intent = Intent(context, MediaPlayerService::class.java)
            intent.apply {
                putExtra(NOTIFICATION_ID, NotificationConstant.PLAYING_MUSIC_NOTIFICATION_ID)
                putExtra(ACTION, ACTION_STOP)
            }
            ContextCompat.startForegroundService(context, intent)
        }

        fun seekMediaPlayer(context: Context, position:Long){
            val intent = Intent(context, MediaPlayerService::class.java)
            intent.apply {
                putExtra(NOTIFICATION_ID, NotificationConstant.PLAYING_MUSIC_NOTIFICATION_ID)
                putExtra(ACTION, ACTION_SEEK_TO)
                putExtra(SEEK_TO_POSITION, position)
            }
            ContextCompat.startForegroundService(context, intent)
            logConsole.d("serviceSeekTo $position")
        }
    }

    private var mediaPlayer:MediaPlayer? = null
    private var durationAudio:Int = 0
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notifHelper = MediaPlayerNotificationHelper(applicationContext)
        val notificationId:Int = intent?.getIntExtra(NOTIFICATION_ID, NotificationConstant.MEDIA_PLAYER_NOTIFICATION_ID) ?: NotificationConstant.MEDIA_PLAYER_NOTIFICATION_ID
        when(intent?.getStringExtra(ACTION)){
            ACTION_PLAY -> {
                prepareAudio()
                val urlAudio = intent.getStringExtra(AUDIO_URL)
                if (urlAudio != null){
                    mediaPlayer = MediaPlayer()
                    mediaPlayer?.setDataSource(applicationContext, Uri.parse(urlAudio))
                    mediaPlayer?.prepareAsync()
                    mediaPlayer?.setOnPreparedListener {
                        durationAudio = it.duration
                        mediaPlayer?.start()
                        val data = getBasedBundle().apply {
                            putString(
                                MediaPlayerNotificationHelper.EXTRA_STATE,
                                MediaPlayerNotificationHelper.STATE_PLAYING
                            )
                        }
                        startForeground(notificationId, notifHelper.getPlayedMediaNotification(data))
                    }
                }
            }
            ACTION_PAUSE -> {
                if(mediaPlayer != null && mediaPlayer?.isPlaying == true){
                    mediaPlayer?.pause()
                    val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    val data = getBasedBundle().apply {
                        putString(
                            MediaPlayerNotificationHelper.EXTRA_STATE,
                            MediaPlayerNotificationHelper.STATE_PAUSED
                        )
                        putInt(MediaPlayerNotificationHelper.EXTRA_CURRENT_POSITION, mediaPlayer!!.currentPosition)
                    }
                    nm.notify(notificationId, notifHelper.getPausedNotification(data))
                }
            }
            ACTION_RESUME -> {
                if (mediaPlayer != null && mediaPlayer?.isPlaying != true){
                    mediaPlayer?.start()
                    val data = getBasedBundle().apply {
                        putString(
                            MediaPlayerNotificationHelper.EXTRA_STATE,
                            MediaPlayerNotificationHelper.STATE_PLAYING
                        )
                        putInt(MediaPlayerNotificationHelper.EXTRA_CURRENT_POSITION, mediaPlayer!!.currentPosition)
                    }
                    startForeground(notificationId, notifHelper.getPlayedMediaNotification(data))
                }
            }
            ACTION_STOP -> {
                stopSelf()
            }
            ACTION_SEEK_TO -> {
                if (mediaPlayer != null){
                    val seekToPosition = intent.getLongExtra(SEEK_TO_POSITION, 0L)
                    mediaPlayer?.seekTo(seekToPosition.toInt())
                    mediaPlayer?.setOnSeekCompleteListener {
                        logConsole.d("setOnSeekCompleteListener $seekToPosition dan ${it.currentPosition}")
                        val state = if (mediaPlayer?.isPlaying == true) MediaPlayerNotificationHelper.STATE_PLAYING else MediaPlayerNotificationHelper.STATE_PAUSED
                        val data = getBasedBundle().apply {
                            putString(MediaPlayerNotificationHelper.EXTRA_STATE, state)
                            putInt(MediaPlayerNotificationHelper.EXTRA_CURRENT_POSITION, mediaPlayer!!.currentPosition)
                        }
                        startForeground(notificationId,
                            if (mediaPlayer?.isPlaying == true) notifHelper.getPlayedMediaNotification(data)
                            else notifHelper.getPausedNotification(data)
                        )
                    }
                }
            }
        }
        return START_STICKY
    }

    private fun getBasedBundle():Bundle {
        return Bundle().apply {
            putInt(MediaPlayerNotificationHelper.EXTRA_DURATION_AUDIO, durationAudio)
        }
    }

    private fun prepareAudio(){
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        super.onDestroy()
    }
}