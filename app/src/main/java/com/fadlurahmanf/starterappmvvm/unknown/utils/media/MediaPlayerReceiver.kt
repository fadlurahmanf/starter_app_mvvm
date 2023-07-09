package com.fadlurahmanf.starterappmvvm.unknown.utils.media

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.fadlurahmanf.starterappmvvm.core.external.constant.logConsole

class MediaPlayerReceiver:BroadcastReceiver() {
    companion object{
        const val ACTION_PAUSE = "com.fadlurahmanf.mediaPlayer.ACTION_PAUSE"
        const val ACTION_RESUME = "com.fadlurahmanf.mediaPlayer.ACTION_RESUME"
        const val ACTION_NEXT = "com.fadlurahmanf.mediaPlayer.ACTION_NEXT"
        const val ACTION_PREVIOUS = "com.fadlurahmanf.mediaPlayer.ACTION_PREVIOUS"
        const val ACTION_STOP = "com.fadlurahmanf.mediaPlayer.ACTION_STOP"
        const val ACTION_SEEK_TO = "com.fadlurahmanf.mediaPlayer.ACTION_SEEK_TO"
        const val SEEK_TO_POSITION = "SEEK_TO_POSITION"

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

        fun getSeekToIntent(context: Context, position:Long):Intent{
            val intent = Intent(context, MediaPlayerReceiver::class.java)
            intent.apply {
                action = ACTION_SEEK_TO
                putExtra(SEEK_TO_POSITION, position)
            }
            logConsole.d("receiver SeekTo $position")
            return intent
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if(context == null) return
        val notifHelper = MediaPlayerNotificationHelper(context)
        val action = intent?.action
        logConsole.d("onReceive action $action")
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
            ACTION_SEEK_TO -> {
                val seekToPosition = intent.getLongExtra(SEEK_TO_POSITION, 0L)
                MediaPlayerService.seekMediaPlayer(context, seekToPosition)
                logConsole.d("receiver ACTION SeekTo $seekToPosition")
            }
        }
    }
}