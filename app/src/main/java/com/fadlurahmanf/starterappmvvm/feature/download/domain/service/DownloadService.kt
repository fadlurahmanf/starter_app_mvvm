package com.fadlurahmanf.starterappmvvm.feature.download.domain.service

import android.app.DownloadManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.content.ContextCompat
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.unknown.data.dto.exception.CustomException
import com.fadlurahmanf.starterappmvvm.feature.download.external.helper.DownloadHelper
import com.fadlurahmanf.starterappmvvm.feature.download.external.helper.DownloadNotificationHelper
import com.fadlurahmanf.starterappmvvm.feature.notification.data.constant.NotificationConstant
import kotlin.random.Random

class DownloadService : Service() {
    private lateinit var downloadHelper: DownloadHelper
    private lateinit var notificationImpl: DownloadNotificationHelper
    private lateinit var handler: Handler
    private var downloadId: Long = 0L
    private val notificationId: Int = NotificationConstant.DEFAULT_DOWNLOAD_NOTIFICATION_ID

    companion object {
        private const val ACTION_DOWNLOAD = "com.fadlurahmanf.download.DOWNLOAD"

        const val DOWNLOAD_URL = "DOWNLOAD_URL"
        const val DOWNLOAD_FILE_NAME = "DOWNLOAD_FILE_NAME"
        fun startService(
            context: Context,
            url: String,
            fileName: String, // file name should include extension
        ) {
            val intent = Intent(context, DownloadService::class.java)
            intent.apply {
                action = ACTION_DOWNLOAD
                putExtra(DOWNLOAD_URL, url)
                putExtra(DOWNLOAD_FILE_NAME, fileName)
            }
            ContextCompat.startForegroundService(context, intent)
        }

        fun stopService(context: Context) {
            val intent = Intent(context, DownloadService::class.java)
            context.stopService(intent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        downloadHelper = DownloadHelper(applicationContext)
        notificationImpl = DownloadNotificationHelper(applicationContext)
        handler = Handler(Looper.getMainLooper())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_DOWNLOAD -> {
                actionDownload(intent)
            }
        }
        return START_STICKY
    }

    private fun actionDownload(intent: Intent?) {
        try {
            startForeground(
                notificationId,
                notificationImpl.notificationPrepareDownload().build()
            )
            val urlDownload = intent?.extras?.getString(DOWNLOAD_URL)
            val downloadFileName = intent?.extras?.getString(DOWNLOAD_FILE_NAME)
            downloadId = downloadHelper.getDownloadId(
                urlDownload ?: "",
                downloadFileName ?: ""
            )
            handler.postDelayed(downloadRunnable, 0)
        } catch (e: Throwable) {
            showError(
                CustomException(
                    idRawMessage = R.string.download_failed_desc
                )
            )
            handler.removeCallbacks(downloadRunnable)
            stopSelf()
        }
    }

    private var downloadRunnable = object : Runnable {
        override fun run() {
            try {
                val status = downloadHelper.updateProgressDownload(downloadId)
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    handler.removeCallbacks(this)
                    stopSelf()
                } else {
                    handler.postDelayed(this, 2000)
                }
            } catch (e: Throwable) {
                showError(
                    CustomException(
                        rawMessage = e.message
                    )
                )
                handler.removeCallbacks(this)
                stopSelf()
            }
        }
    }

    fun showError(e: CustomException) {
        logConsole.e("DOWNLOAD FAILED: ${e.toProperException(applicationContext)}")
        notificationImpl.showNotification(
            id = Random.nextInt(1000),
            title = e.title ?: "-",
            body = e.message ?: "-"
        )
    }

    override fun onDestroy() {
        handler.removeCallbacks(downloadRunnable)
        super.onDestroy()
    }
}