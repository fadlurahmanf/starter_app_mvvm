package com.fadlurahmanf.starterappmvvm.utils.media

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.fadlurahmanf.starterappmvvm.constant.NotificationConstant
import com.fadlurahmanf.starterappmvvm.utils.logging.logd

class MediaPlayerService:Service() {
    companion object{
        const val AUDIO_URL = "AUDIO_URL"
        const val NOTIFICATION_ID = "NOTIFICATION_ID"
        const val ACTION = "ACTION"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_RESUME = "ACTION_RESUME"
        const val ACTION_STOP = "ACTION_STOP"
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
    }

    private var mediaPlayer:MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notifHelper = MediaPlayerNotificationHelper(applicationContext)
        val notificationId:Int = intent?.getIntExtra(NOTIFICATION_ID, NotificationConstant.DOWNLOAD_NOTIFICATION_ID) ?: NotificationConstant.DOWNLOAD_NOTIFICATION_ID
        when(intent?.getStringExtra(ACTION)){
            ACTION_PLAY -> {
                prepareAudio()
                val urlAudio = intent.getStringExtra(AUDIO_URL)
                if (urlAudio != null){
                    mediaPlayer = MediaPlayer()
                    mediaPlayer?.setDataSource(applicationContext, Uri.parse(urlAudio))
                    mediaPlayer?.prepareAsync()
                    mediaPlayer?.setOnPreparedListener {
                        mediaPlayer?.start()
                        startForeground(notificationId, notifHelper.getPlayedMediaNotification())
                    }
                }
            }
            ACTION_PAUSE -> {
                if(mediaPlayer?.isPlaying == true){
                    mediaPlayer?.pause()
                    val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    nm.notify(notificationId, notifHelper.getPausedNotification())
                }
            }
            ACTION_RESUME -> {
                if (mediaPlayer != null && mediaPlayer?.isPlaying != true){
                    mediaPlayer?.start()
                    val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    nm.notify(notificationId, notifHelper.getPlayedMediaNotification())
                    startForeground(notificationId, notifHelper.getPlayedMediaNotification())
                }
            }
            ACTION_STOP -> {
                stopSelf()
            }
        }
        return START_STICKY
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