package com.fadlurahmanf.starterappmvvm.utils.notification

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.*
import android.util.Log

class NotificationPlayerService:Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.d("NotifService", "onCreate")
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        super.onCreate()
    }

    private fun prepareMediaPlayer(){
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        vibrator?.cancel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("NotifService", "onStartCommand")
        prepareMediaPlayer()
        startMediaPlayer()
        playVibrator()
        return Service.START_NOT_STICKY
    }

    private var vibrator:Vibrator? = null
    private var audioManager:AudioManager? = null
    private fun playVibrator(){
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
        when(audioManager?.ringerMode){
            AudioManager.RINGER_MODE_SILENT -> {}
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0L, 2000L, 2000L), 0))
                }else{
                    vibrator?.vibrate(longArrayOf(0L, 2000L, 2000L), 0)
                }
            }
        }
    }

    private var mediaPlayer:MediaPlayer? = null
    private fun startMediaPlayer(){
        when(audioManager?.ringerMode){
            AudioManager.RINGER_MODE_NORMAL -> {
                val notifUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                mediaPlayer = MediaPlayer()
                mediaPlayer?.setDataSource(applicationContext, notifUri)
                mediaPlayer?.prepare()
                mediaPlayer?.isLooping = true
                mediaPlayer?.start()
            }
        }
    }

    override fun onDestroy() {
        Log.d("NotifService", "onDestroy")
        vibrator?.cancel()
        vibrator = null
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }
}