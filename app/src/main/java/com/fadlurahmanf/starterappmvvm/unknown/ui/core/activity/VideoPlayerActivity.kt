package com.fadlurahmanf.starterappmvvm.unknown.ui.core.activity

import android.app.AlertDialog
import android.app.PictureInPictureParams
import android.app.PictureInPictureUiState
import android.os.Build
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.core.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.databinding.ActivityVideoPlayerBinding
import com.fadlurahmanf.starterappmvvm.unknown.utils.video_player.VideoPlayerHelper
import com.google.android.exoplayer2.Player

class VideoPlayerActivity : BaseActivity<ActivityVideoPlayerBinding>(ActivityVideoPlayerBinding::inflate) {
    private lateinit var videoPlayerHelper: VideoPlayerHelper
    private var videoFormatList = arrayListOf<VideoPlayerHelper.QualityVideoFormat>()
    private var currentFormatId:String? = null
    private var currentQFormat: VideoPlayerHelper.QualityVideoFormat? = null
    private val listener = object : VideoPlayerHelper.Listener {
        override fun onProgressChanged(position: Long) {
            binding.seekBar.progress = position.toInt()
        }

        override fun onDurationChanged(duration: Long) {
            super.onDurationChanged(duration)
            binding.seekBar.max = duration.toInt()
        }

        override fun onGetQualityOfVideoChanged(qualities: List<VideoPlayerHelper.QualityVideoFormat>) {
            super.onGetQualityOfVideoChanged(qualities)
            videoFormatList.clear()
            videoFormatList.addAll(qualities)
            videoFormatList.forEach {
                logConsole.d("onGetQualityOfVideoChanged: \nformatId:${it.formatId}\nwidth x height:${it.rawFormat?.width}x${it.rawFormat?.height}\nformatPixel:${"${it.formatPixel}p"}\nformatName:${it.formatName}")
            }
        }

        override fun onQualityVideoChanged(format: VideoPlayerHelper.QualityVideoFormat) {
            super.onQualityVideoChanged(format)
            logConsole.d("onQualityVideoChanged ${format.formatId} & ${"${format.formatPixel}p"} & ${format.formatName}")
            currentFormatId = format.formatId
            currentQFormat = format
            binding.tvQuality.text = format.formatName ?: "Auto"
        }

        override fun onPlayerStateChanged(state: Int) {
            super.onPlayerStateChanged(state)
            if (state == Player.STATE_IDLE || state == Player.STATE_ENDED){
                binding.ivPlayOrPause.setImageDrawable(ContextCompat.getDrawable(this@VideoPlayerActivity, R.drawable.ic_round_play_arrow))
            }else{
                binding.ivPlayOrPause.setImageDrawable(ContextCompat.getDrawable(this@VideoPlayerActivity, R.drawable.ic_round_pause))
            }
        }
    }

    override fun initSetup() {
        videoPlayerHelper = VideoPlayerHelper(this)
        videoPlayerHelper.addListener(listener)
        binding.exoPlayer.player = videoPlayerHelper.exoPlayer
        binding.exoPlayer.useController = false
        videoPlayerHelper.playHlsRemoteAudio("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")
        initAction()
    }

    private fun initAction() {
        binding.tvQuality.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val array = arrayOfNulls<String>(videoFormatList.size)
            var checkedItem:Int = 0
            for (i in videoFormatList.indices){
                if (videoFormatList[i].formatId == currentFormatId){
                    checkedItem = i
                }
                array[i] = videoFormatList[i].formatName ?: "-"
            }
            builder.setNegativeButton("Cancel"){
                dialog, _ ->
                dialog.dismiss()
            }
            builder.setPositiveButton("Apply"){
                dialog, _ ->
                if (videoFormatList[checkedItem].formatId != null){
                    videoPlayerHelper.selectQualityOfVideo(videoFormatList[checkedItem].formatId!!)
                }
                dialog.dismiss()
            }
            builder.setSingleChoiceItems(array, checkedItem
            ) { _, which ->
                checkedItem = which
            }

            builder.create().show()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                videoPlayerHelper.exoPlayer.seekTo(progress.toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                videoPlayerHelper.exoPlayer.playWhenReady = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                videoPlayerHelper.exoPlayer.playWhenReady = true
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayerHelper.removeListener()
        videoPlayerHelper.destroyExoPlayer()
    }

    override fun inject() {

    }

    override fun onStop() {
        super.onStop()
        videoPlayerHelper.removeListener()
        videoPlayerHelper.destroyExoPlayer()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onUserLeaveHint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            enterPictureInPictureMode(PictureInPictureParams.Builder()
                .build())
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            enterPictureInPictureMode()
        }
        super.onUserLeaveHint()
    }

    override fun onPictureInPictureUiStateChanged(pipState: PictureInPictureUiState) {
        super.onPictureInPictureUiStateChanged(pipState)
    }
}