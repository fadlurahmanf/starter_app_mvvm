package com.fadlurahmanf.starterappmvvm.utils.media

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.fadlurahmanf.starterappmvvm.utils.logging.logd

class MediaPlayerReceiver:BroadcastReceiver() {
    companion object{
        const val ACTION_PAUSE = "com.fadlurahmanf.mediaPlayer.ACTION_PAUSE"
        const val ACTION_RESUME = "com.fadlurahmanf.mediaPlayer.ACTION_RESUME"
        const val ACTION_NEXT = "com.fadlurahmanf.mediaPlayer.ACTION_NEXT"
        const val ACTION_PREVIOUS = "com.fadlurahmanf.mediaPlayer.ACTION_PREVIOUS"
        const val ACTION_STOP = "com.fadlurahmanf.mediaPlayer.ACTION_STOP"

        fun getResume(context: Context):Intent{
            val intent = Intent(context, MediaPlayerReceiver::class.java)
            intent.apply {
                action = ACTION_RESUME
            }
            return intent
        }

        fun getPauseIntent(context: Context):Intent{
            val intent = Intent(context, MediaPlayerReceiver::class.java)
            intent.apply {
                action = ACTION_PAUSE
            }
            return intent
        }

        fun getStopIntent(context: Context):Intent{
            val intent = Intent(context, MediaPlayerReceiver::class.java)
            intent.apply {
                action = ACTION_STOP
            }
            return intent
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if(context == null) return
        val notifHelper = MediaPlayerNotificationHelper(context)
        val action = intent?.action
        logd("masuk action ${action}")
        when(action){
            ACTION_PAUSE -> {
                MediaPlayerService.pauseAudio(context)
            }
            ACTION_RESUME -> {
                MediaPlayerService.resumeAudio(context)
            }
            ACTION_STOP -> {
                MediaPlayerService.stopAudio(context)
            }
        }
    }
}