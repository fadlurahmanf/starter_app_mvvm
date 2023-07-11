package com.fadlurahmanf.starterappmvvm.feature.download.domain.service

import android.app.DownloadManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.content.ContextCompat
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.data.dto.exception.CustomException
import com.fadlurahmanf.starterappmvvm.feature.download.external.helper.DownloadNotificationHelper
import com.fadlurahmanf.starterappmvvm.feature.notification.data.constant.NotificationConstant
import java.io.File
import kotlin.random.Random

class DownloadService : Service() {
    private lateinit var downloadManager: DownloadManager
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
        downloadManager =
            applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
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
        startForeground(
            notificationId,
            notificationImpl.notificationPrepareDownload().build()
        )
        val urlDownload = intent?.extras?.getString(DOWNLOAD_URL)
        val downloadFileName = intent?.extras?.getString(DOWNLOAD_FILE_NAME)
        if (urlDownload.isNullOrEmpty()) {
            showError(
                CustomException(
                    idRawMessage = R.string.download_url_not_valid_desc
                )
            )
            stopSelf()
            return
        }
        val file =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path)
        val downloadRequest = DownloadManager.Request(Uri.parse(urlDownload))
        downloadRequest.apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        }
        val uri =
            Uri.withAppendedPath(Uri.fromFile(file), "$downloadFileName")
        downloadRequest.setDestinationUri(uri)
        downloadId = downloadManager.enqueue(downloadRequest)
        handler.postDelayed(downloadRunnable, 0)
    }

    private var downloadRunnable = object : Runnable {
        override fun run() {
            try {
                val cursor =
                    downloadManager.query(DownloadManager.Query().setFilterById(downloadId))
                if (cursor.moveToFirst()) {
                    val status: Int = getStatus(cursor)
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        handler.removeCallbacks(this)
                        stopSelf()
                        return
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        showError(
                            CustomException(
                                idRawMessage = R.string.download_failed_desc
                            )
                        )
                        handler.removeCallbacks(this)
                        stopSelf()
                        return
                    }
                    val totalSize: Int = getTotal(cursor)
                    val progress: Int = getProgress(cursor)
                    if (totalSize >= 0 && progress >= 0) {
                        val data = Bundle()
                        data.apply {
                            putInt(DownloadNotificationHelper.EXTRA_TOTAL_SIZE, totalSize)
                            putInt(
                                DownloadNotificationHelper.EXTRA_CURRENT_PROGRESS_SIZE,
                                progress
                            )
                        }
                        if (totalSize < progress) {
                            notificationImpl.showNotification(
                                title = applicationContext.getString(R.string.download),
                                body = applicationContext.getString(R.string.downloading)
                            )
                        } else {
                            notificationImpl.showDownload(data)
                        }
                    }
                }
                handler.postDelayed(this, 2000)
            } catch (e: Throwable) {
                showError(
                    CustomException(
                        rawMessage = e.message
                    )
                )
                stopSelf()
                handler.removeCallbacks(this)
                return
            }
        }
    }

    private fun getStatus(cursor: Cursor): Int {
        var status: Int = -1
        if (cursor.getColumnIndex(DownloadManager.COLUMN_STATUS) >= 0) {
            status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
        }
        return status
    }

    private fun getTotal(cursor: Cursor): Int {
        var totalSize: Int = -1
        if (cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES) >= 0) {
            totalSize =
                cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
        }
        return totalSize
    }

    private fun getProgress(cursor: Cursor): Int {
        var progress: Int = -1
        if (cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR) >= 0) {
            progress =
                cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
        }
        return progress
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