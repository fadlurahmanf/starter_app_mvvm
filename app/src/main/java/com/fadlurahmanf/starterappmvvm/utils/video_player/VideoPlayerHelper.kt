package com.fadlurahmanf.starterappmvvm.utils.video_player

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroup
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionOverride
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.FileDataSource
import java.io.File
import kotlin.math.max

class VideoPlayerHelper(var context: Context) {

    data class QualityVideoFormat(
        var formatId: String? = null,
        var formatName:String? = null,
        var formatPixel:Int? = null,
        var rawFormat:Format? = null
    )

    enum class OutputAudioDevice {
        SPEAKER, WIRE, BLUETOOTH
    }
    var exoPlayer: ExoPlayer
    private var callback: VideoPlayerHelper.Listener? = null
    private var trackSelector: TrackSelector = DefaultTrackSelector(context, AdaptiveTrackSelection.Factory())
    private var handler: Handler
    private var audioManager: AudioManager

    var state = Player.STATE_IDLE
    var isPlaying = false
    var currentQFormat:QualityVideoFormat? = null
    var outputAudio:OutputAudioDevice? = null

    private val exoPlayerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            if (callback != null) {
                state = playbackState
                callback!!.onPlayerStateChanged(playbackState)
                if (playbackState == Player.STATE_READY){
                    getQualityOfVideo()
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            if (callback != null){
                this@VideoPlayerHelper.isPlaying = isPlaying
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

    fun addListener(listener: Listener) {
        this.callback = listener
    }

    fun removeListener(){
        this.callback = null
    }

    private fun mediaSourceFromFile(file: File) =
        ProgressiveMediaSource.Factory(FileDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(Uri.fromFile(file)))

    /**
     * media source from internet, extension usually mp4, doesnt contain multiple quality format
     */
    private fun mediaSourceFromNetwork(url: String) =
        ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(Uri.parse(url)))

    /**
     * media source that contain multiple quality format, extension usually different from mp4
     */
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

    /**
     * Playing video from internet with HLS type
     * HLS support multiple quality
     *
     * Params:
     * */
    fun playHlsRemoteAudio(url: String){
        exoPlayer.setMediaSource(mediaSourceFromHLS(url))
        exoPlayer.prepare()
        handler.postDelayed(runnable, 1000)
    }

    private lateinit var trackGroup: TrackGroup
    private fun getQualityOfVideo(){
        val map = hashMapOf<String, QualityVideoFormat>()
        val groups = exoPlayer.currentTracks.groups.filter {
            it.type == C.TRACK_TYPE_VIDEO
        }
        if (groups.isNotEmpty()){
            val group = groups[0]
            trackGroup = group.mediaTrackGroup
            for (i in 0 until trackGroup.length){
                val format = trackGroup.getFormat(i)
                if (format.id != null){
                    val qFormat = convertFormatToQualityVideoFormat(format = format)
                    map[format.id!!] = qFormat
                }
            }
            if(callback != null){
                callback!!.onGetQualityOfVideoChanged(map.values.toList())
            }
            val currentFormat = exoPlayer.videoFormat
            if (callback != null && currentFormat?.id != null){
                this.currentQFormat = convertFormatToQualityVideoFormat(currentFormat)
                currentFormatId = this.currentQFormat?.formatId
                callback!!.onQualityVideoChanged(format = this.currentQFormat!!)
            }
        }
    }

    private fun convertFormatToQualityVideoFormat(format: Format): QualityVideoFormat {
        val max = max(format.height, format.width)
        return QualityVideoFormat(
            formatId = format.id,
            rawFormat = format,
            formatName = "${max}p",
            formatPixel = max
        )
    }

    fun selectQualityOfVideo(formatId: String){
        var index:Int? = null
        for (i in 0 until trackGroup.length){
            val format = trackGroup.getFormat(i)
            if (format.id == formatId){
                index = i
                break
            }
        }
        if (index != null){
            trackSelector.parameters = trackSelector.parameters.buildUpon()
                .addOverride(TrackSelectionOverride(trackGroup, index))
                .build()
        }
    }

    private var currentFormatId:String? = null
    private fun checkVideoQualityChanged(){
        val format = exoPlayer.videoFormat
        if (exoPlayer.videoFormat?.id != null
            && currentFormatId != exoPlayer.videoFormat?.id){
            currentFormatId = exoPlayer.videoFormat?.id
            this.currentQFormat = convertFormatToQualityVideoFormat(format = format!!)
            if (callback != null){
                callback!!.onQualityVideoChanged(this.currentQFormat!!)
            }
        }
    }

    private fun checkAudioOutput() {
        val isWiredHeadsetOn = audioManager.isWiredHeadsetOn
        val isBluetoothA2dpOn = audioManager.isBluetoothA2dpOn
        val isBluetoothScoOn = audioManager.isBluetoothScoOn
        val isSpeakerphoneOn = audioManager.isSpeakerphoneOn
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val productName = audioManager.communicationDevice?.productName
            when (audioManager.communicationDevice?.type) {
                AudioDeviceInfo.TYPE_BLUETOOTH_SCO, AudioDeviceInfo.TYPE_BLUETOOTH_A2DP -> {
                    outputAudio = OutputAudioDevice.BLUETOOTH
                    if (callback != null){
                        callback!!.onAudioOutputChanged(OutputAudioDevice.BLUETOOTH)
                    }
                }
                AudioDeviceInfo.TYPE_WIRED_HEADSET, AudioDeviceInfo.TYPE_WIRED_HEADPHONES -> {
                    outputAudio = OutputAudioDevice.WIRE
                    if (callback != null){
                        callback!!.onAudioOutputChanged(OutputAudioDevice.WIRE)
                    }
                }
                else -> {
                    outputAudio = OutputAudioDevice.SPEAKER
                    if (callback != null){
                        callback!!.onAudioOutputChanged(OutputAudioDevice.SPEAKER)
                    }
                }
            }
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val output = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
            output.forEach {
                println("masuk ${it.productName}")
            }
            if(isBluetoothA2dpOn || isBluetoothScoOn){
                outputAudio = OutputAudioDevice.BLUETOOTH
                if (callback != null){
                    callback!!.onAudioOutputChanged(OutputAudioDevice.BLUETOOTH)
                }
            }else if(isWiredHeadsetOn){
                outputAudio = OutputAudioDevice.SPEAKER
                if (callback != null){
                    callback!!.onAudioOutputChanged(OutputAudioDevice.SPEAKER)
                }
            }else {
                outputAudio = OutputAudioDevice.SPEAKER
                if (callback != null){
                    callback!!.onAudioOutputChanged(OutputAudioDevice.SPEAKER)
                }
            }
        }else{
            // unknown
        }
    }

    private fun checkAudioPosition(){
        if (callback != null){
            callback!!.onDurationChanged(exoPlayer.duration)
            callback!!.onProgressChanged(exoPlayer.currentPosition)
        }
    }

    interface Listener {
        fun onDurationChanged(duration:Long){}
        fun onProgressChanged(position:Long)
        fun onPlayerStateChanged(state: Int){}
        fun onIsPlayingChanged(isPlaying: Boolean){}
        fun onAudioOutputChanged(output: OutputAudioDevice){}
        fun onGetQualityOfVideoChanged(qualities: List<QualityVideoFormat>){}
        fun onQualityVideoChanged(format:QualityVideoFormat){}
    }

    private val runnable = object : Runnable {
        override fun run() {
            checkVideoQualityChanged()
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