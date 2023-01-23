package com.fadlurahmanf.starterappmvvm.ui.core.activity

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.media.tv.TvTrackInfo.TYPE_VIDEO
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Pair
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityVideoPlayerBinding
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Tracks
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroup
import com.google.android.exoplayer2.source.hls.HlsDataSourceFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector.SelectionOverride
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer
import java.io.File


class VideoPlayerActivity :
    BaseActivity<ActivityVideoPlayerBinding>(ActivityVideoPlayerBinding::inflate) {
    private lateinit var baseVideoPlayer: BaseVideoPlayer

    private var listener = object : BaseVideoPlayer.BaseVideoPlayerListener {
        override fun onProgressChanged(position: Long) {

        }
    }

    override fun initSetup() {
        baseVideoPlayer = BaseVideoPlayer(this)
        baseVideoPlayer.addListener(listener)
        binding.exoPlayer.player = baseVideoPlayer.exoPlayer

        baseVideoPlayer.playHlsRemoteAudio("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")
    }

    override fun onDestroy() {
        super.onDestroy()
        baseVideoPlayer.removeListener()
        baseVideoPlayer.destroyExoPlayer()
    }

    override fun inject() {

    }
}

enum class OutputAudioDevice {
    SPEAKER, BLUETOOTH, WIRE
}

class BaseVideoPlayer(var context: Context) {
    var exoPlayer: ExoPlayer
    private var callback: BaseVideoPlayerListener? = null
    private var trackSelector:TrackSelector = DefaultTrackSelector(context, AdaptiveTrackSelection.Factory())
    private var handler: Handler
    private var audioManager: AudioManager

    private val exoPlayerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            if (callback != null) {
                callback!!.onPlayerStateChanged(playbackState)

                if (playbackState == Player.STATE_READY){

                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            if (callback != null){
                callback!!.onIsPlayingChanged(isPlaying)
            }
        }
    }


    init {
        exoPlayer = ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()

        exoPlayer.playWhenReady = true
        exoPlayer.addListener(exoPlayerListener)
        handler = Handler(Looper.getMainLooper())
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    fun addListener(listener: BaseVideoPlayerListener) {
        this.callback = listener
    }

    fun removeListener(){
        this.callback = null
    }

    private fun mediaSourceFromFile(file: File) =
        ProgressiveMediaSource.Factory(FileDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(Uri.fromFile(file)))

    private fun mediaSourceFromNetwork(url: String) =
        ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(Uri.parse(url)))

    private fun mediaSourceFromHLS(url: String): HlsMediaSource {
        val dataSourceFactory = DefaultHttpDataSource.Factory()
        return HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(Uri.parse(url)))
    }

    /**
     * Playing video from internet
     * */
    fun playRemoteVideo(url: String) {
        exoPlayer.setMediaSource(mediaSourceFromNetwork(url))
        exoPlayer.prepare()
        handler.postDelayed(runnable, 1000)
    }

    fun playHlsRemoteAudio(url: String){
        exoPlayer.setMediaSource(mediaSourceFromHLS(url))
        exoPlayer.prepare()
        handler.postDelayed(runnable, 1000)
    }

    private fun getQualityOfVideo(){
        val map = hashMapOf<String, Format>()
        var trackGroup:TrackGroup? = null
        exoPlayer.currentTracks.groups.filter {
            it.type == C.TRACK_TYPE_VIDEO
        }.forEach {
            trackGroup = it.mediaTrackGroup
            println("masuk batas")
            for (i in 0 until it.length){
                map[it.getTrackFormat(i).toString()] = it.getTrackFormat(i)
                println("masuk ${it.getTrackFormat(i).height} x ${it.getTrackFormat(i).width}")
            }
        }
        callback!!.onGetQualityOfVideoChanged(map)
        println("masuk sekarang ${exoPlayer.videoFormat?.height} and ${exoPlayer.videoFormat?.width}")
//        trackSelector.parameters = trackSelector.parameters.buildUpon()
//            .addOverride(TrackSelectionOverride(trackGroup!!, 0))
//            .build()
    }

    private fun checkAudioOutput() {
        if (exoPlayer.playbackState == Player.STATE_READY){
            getQualityOfVideo()
        }
        val isWiredHeadsetOn = audioManager.isWiredHeadsetOn
        val isBluetoothA2dpOn = audioManager.isBluetoothA2dpOn
        val isBluetoothScoOn = audioManager.isBluetoothScoOn
        val isSpeakerphoneOn = audioManager.isSpeakerphoneOn
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val type = audioManager.communicationDevice?.type
            if (callback != null){
                when (type) {
                    AudioDeviceInfo.TYPE_BLUETOOTH_SCO, AudioDeviceInfo.TYPE_BLUETOOTH_A2DP -> {
                        callback!!.onAudioOutputChanged(OutputAudioDevice.BLUETOOTH)
                    }
                    AudioDeviceInfo.TYPE_WIRED_HEADSET, AudioDeviceInfo.TYPE_WIRED_HEADPHONES -> {
                        callback!!.onAudioOutputChanged(OutputAudioDevice.WIRE)
                    }
                    else -> {
                        callback!!.onAudioOutputChanged(OutputAudioDevice.SPEAKER)
                    }
                }
            }
        }else{
            if (callback != null){
                if(isBluetoothA2dpOn || isBluetoothScoOn){
                    callback!!.onAudioOutputChanged(OutputAudioDevice.BLUETOOTH)
                }else if(isWiredHeadsetOn){
                    callback!!.onAudioOutputChanged(OutputAudioDevice.SPEAKER)
                }else {
                    callback!!.onAudioOutputChanged(OutputAudioDevice.SPEAKER)
                }
            }
        }
    }

    private fun checkAudioPosition(){
        if (callback != null){
            callback!!.onDurationChanged(exoPlayer.duration)
            callback!!.onProgressChanged(exoPlayer.currentPosition)
        }
    }

    interface BaseVideoPlayerListener {
        fun onDurationChanged(duration:Long){}
        fun onPlayerStateChanged(state: Int){}
        fun onIsPlayingChanged(isPlaying: Boolean){}
        fun onAudioOutputChanged(output:OutputAudioDevice){}
        fun onProgressChanged(position:Long)
        fun onGetQualityOfVideoChanged(map: HashMap<String, Format>){}
    }

    private val runnable = object : Runnable {
        override fun run() {
            checkAudioOutput()
            checkAudioPosition()
            handler.postDelayed(this, 1000)
        }
    }

    fun destroyExoPlayer() {
        handler.removeCallbacks(runnable)
        exoPlayer.stop()
        exoPlayer.release()
    }
}
