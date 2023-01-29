package com.fadlurahmanf.starterappmvvm.ui.core.activity

import android.app.AlertDialog
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityVideoPlayerBinding
import com.fadlurahmanf.starterappmvvm.utils.logging.logd
import com.fadlurahmanf.starterappmvvm.utils.video_player.VideoPlayerHelper
import com.github.rubensousa.previewseekbar.PreviewBar
import com.github.rubensousa.previewseekbar.PreviewLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class VideoPlayerActivity : BaseActivity<ActivityVideoPlayerBinding>(ActivityVideoPlayerBinding::inflate) {
    private lateinit var videoPlayerHelper: VideoPlayerHelper
    private var videoFormatList = arrayListOf<VideoPlayerHelper.QualityVideoFormat>()
    private var currentFormatId:String? = null
    private var currentQFormat:VideoPlayerHelper.QualityVideoFormat? = null
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
                logd("onGetQualityOfVideoChanged: \nformatId:${it.formatId}\nwidth x height:${it.rawFormat?.width}x${it.rawFormat?.height}\nformatPixel:${"${it.formatPixel}p"}\nformatName:${it.formatName}")
            }
        }

        override fun onQualityVideoChanged(format: VideoPlayerHelper.QualityVideoFormat) {
            super.onQualityVideoChanged(format)
            logd("onQualityVideoChanged ${format.formatId} & ${"${format.formatPixel}p"} & ${format.formatName}")
            currentFormatId = format.formatId
            currentQFormat = format
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
        binding.ivPause.setOnClickListener {
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
        val previewLoader = PreviewLoader { currentPosition, max ->
            GlobalScope.launch(Dispatchers.IO) {
                println("MASUK START $currentPosition")
                val bitmap = getBitmap(currentPosition)
                println("MASUK END ${bitmap != null}")
            }
        }
        binding.seekBar.setPreviewLoader(previewLoader)
        binding.seekBar.max = 1


        binding.seekBar.addOnScrubListener(object : PreviewBar.OnScrubListener{
            override fun onScrubStart(previewBar: PreviewBar?) {
                videoPlayerHelper.exoPlayer.playWhenReady = false
            }

            override fun onScrubMove(previewBar: PreviewBar?, progress: Int, fromUser: Boolean) {
                videoPlayerHelper.exoPlayer.seekTo(progress.toLong())
            }

            override fun onScrubStop(previewBar: PreviewBar?) {
                videoPlayerHelper.exoPlayer.playWhenReady = true
            }

        })
    }

    private val retriever = MediaMetadataRetriever()
    private fun getBitmap(time:Long):Bitmap?{
        try {
            retriever.setDataSource(this, Uri.parse("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8"))
            return retriever.getFrameAtTime(time, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        }catch (e:Exception){
            e.printStackTrace()
        } finally {
            try {
                retriever.release()
            } catch (e:Exception){
                e.printStackTrace()
            }
        }
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayerHelper.removeListener()
        videoPlayerHelper.destroyExoPlayer()
    }

    override fun inject() {

    }
}