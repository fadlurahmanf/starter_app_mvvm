package com.fadlurahmanf.starterappmvvm.ui.core.activity

import android.app.AlertDialog
import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityVideoPlayerBinding
import com.fadlurahmanf.starterappmvvm.utils.logging.logd
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroup
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.*
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.FileDataSource
import java.io.File
import kotlin.math.max


class VideoPlayerActivity : BaseActivity<ActivityVideoPlayerBinding>(ActivityVideoPlayerBinding::inflate) {
    private lateinit var baseVideoPlayer: BaseVideoPlayer

    private var videoFormatList = arrayListOf<Format>()
    private var currentFormatId:String? = null
    private val listener = object : BaseVideoPlayer.BaseVideoPlayerListener {
        override fun onProgressChanged(position: Long) {

        }

        override fun onGetQualityOfVideoChanged(map: HashMap<String, Format>) {
            super.onGetQualityOfVideoChanged(map)
            videoFormatList.clear()
            val values = map.values.toList()
            for (i in values.indices){
                logd("onGetQualityOfVideoChanged ${values[i].width}x${values[i].height} & ${"${max(values[i].width, values[i].height)}p"}")
                videoFormatList.add(values[i])
            }
        }

        override fun onQualityVideoChanged(formatId: String, pixel: Int) {
            super.onQualityVideoChanged(formatId, pixel)
            logd("onQualityVideoChanged $formatId & ${"${pixel}p"}")
            currentFormatId = formatId
        }
    }

    override fun initSetup() {
        baseVideoPlayer = BaseVideoPlayer(this)
        baseVideoPlayer.addListener(listener)
        binding.exoPlayer.player = baseVideoPlayer.exoPlayer
        binding.exoPlayer.useController = false

        baseVideoPlayer.playHlsRemoteAudio("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")

        initAction()
    }

    private fun initAction() {
        binding.ivPause.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val array = arrayOfNulls<String>(videoFormatList.size)
            var checkedItem:Int = 0
            for (i in videoFormatList.indices){
                val width = videoFormatList[i].width
                val height = videoFormatList[i].height
                val max = max(width, height)
                if (videoFormatList[i].id == currentFormatId){
                    checkedItem = i
                }
                array[i] = "${max}p"
            }
            builder.setNegativeButton("Cancel"){
                dialog, _ ->
                dialog.dismiss()
            }
            builder.setPositiveButton("Apply"){
                dialog, _ ->
                if (videoFormatList[checkedItem].id != null){
                    baseVideoPlayer.selectQualityOfVideo(videoFormatList[checkedItem].id!!)
                }
                dialog.dismiss()
            }
            builder.setSingleChoiceItems(array, checkedItem
            ) { _, which ->
                checkedItem = which
            }

            builder.create().show()
        }
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
                    getQualityOfVideo()
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

    /**
     * Playing video from internet with HLS type
     * HLS support multiple quality
     * */
    fun playHlsRemoteAudio(url: String){
        exoPlayer.setMediaSource(mediaSourceFromHLS(url))
        exoPlayer.prepare()
        handler.postDelayed(runnable, 1000)
    }

    private lateinit var trackGroup:TrackGroup
    private fun getQualityOfVideo(){
        val map = hashMapOf<String, Format>()
        val groups = exoPlayer.currentTracks.groups.filter {
            it.type == C.TRACK_TYPE_VIDEO
        }
        if (groups.isNotEmpty()){
            val group = groups[0]
            trackGroup = group.mediaTrackGroup
            for (i in 0 until trackGroup.length){
                val format = trackGroup.getFormat(i)
                if (format.id != null){
                    map[format.id!!] = format
                }
            }
            callback!!.onGetQualityOfVideoChanged(map)
            val currentFormat = exoPlayer.videoFormat
            val max = max(currentFormat?.height?:0, currentFormat?.width?:0)
            if (currentFormat?.id != null){
                callback!!.onQualityVideoChanged(currentFormat.id!!, max)
            }
        }
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
        val width = exoPlayer.videoFormat?.width
        val height = exoPlayer.videoFormat?.height
        val max = max(width?:0, height?:0)
        if (exoPlayer.videoFormat?.id != null && exoPlayer.videoFormat?.id != null
            && currentFormatId != exoPlayer.videoFormat?.id){
            if (callback != null){
                currentFormatId = exoPlayer.videoFormat?.id
                callback!!.onQualityVideoChanged(exoPlayer.videoFormat!!.id!!, max)
            }
        }
    }

    private fun checkAudioOutput() {
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
        fun onQualityVideoChanged(formatId:String, pixel:Int){}
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
