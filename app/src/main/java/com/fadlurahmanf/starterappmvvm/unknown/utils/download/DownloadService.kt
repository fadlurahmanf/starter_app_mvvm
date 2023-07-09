package com.fadlurahmanf.starterappmvvm.unknown.utils.download

import android.app.DownloadManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.content.ContextCompat
import com.fadlurahmanf.starterappmvvm.core.data.constant.NotificationConstant
import java.io.File
import kotlin.random.Random

class DownloadService : Service() {
    private lateinit var downloadManager: DownloadManager
    private lateinit var notificationHelper: DownloadNotificationHelper
    private lateinit var handler: Handler
    private var downloadId: Long = 0L
    private var notificationId: Int = 0

    companion object {
        const val DOWNLOAD_URL = "DOWNLOAD_URL"
        fun startService(
            context: Context,
            url: String,
        ) {
            val intent = Intent(context, DownloadService::class.java)
            intent.apply {
                putExtra(
                    DownloadNotificationHelper.EXTRA_NOTIFICATION_ID,
                    NotificationConstant.DOWNLOAD_NOTIFICATION_ID
                )
                putExtra(DOWNLOAD_URL, url)
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
        notificationHelper = DownloadNotificationHelper(applicationContext)
        handler = Handler(Looper.getMainLooper())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notifHelper = DownloadNotificationHelper(context = applicationContext)
        val notificationId: Int? =
            intent?.extras?.getInt(DownloadNotificationHelper.EXTRA_NOTIFICATION_ID)
        val urlDownload = intent?.extras?.getString(DOWNLOAD_URL)
        if (notificationId == null || urlDownload == null) {
            stopSelf()
        }
        this.notificationId = notificationId!!
        startForeground(notificationId, notifHelper.prepareDownload())
        val file =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path)
        val downloadRequest = DownloadManager.Request(Uri.parse(urlDownload!!))
        downloadRequest.apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        }
        val uri =
            Uri.withAppendedPath(Uri.fromFile(file), "starter_app_mvvm_${Random.nextInt(999)}.mp4")
        downloadRequest.setDestinationUri(uri)
        downloadId = downloadManager.enqueue(downloadRequest)
        handler.postDelayed(downloadRunnable, 0)
        return START_STICKY
    }

    private var downloadRunnable = object : Runnable {
        override fun run() {
            val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))
            if (cursor.moveToFirst()) {
                val status: Int
                if (cursor.getColumnIndex(DownloadManager.COLUMN_STATUS) >= 0) {
                    status =
                        cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                    if (status == DownloadManager.STATUS_SUCCESSFUL
                        || status == DownloadManager.STATUS_FAILED
                    ) {
                        handler.removeCallbacks(this)
                        stopSelf()
                        return
                    }
                }
                val totalSize: Int
                if (cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES) >= 0) {
                    totalSize =
                        cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                    val currentSize: Int
                    if (cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR) >= 0) {
                        currentSize =
                            cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                        val data = Bundle()
                        data.apply {
                            putInt(DownloadNotificationHelper.EXTRA_NOTIFICATION_ID, notificationId)
                            putInt(DownloadNotificationHelper.EXTRA_TOTAL_SIZE, totalSize)
                            putInt(
                                DownloadNotificationHelper.EXTRA_CURRENT_PROGRESS_SIZE,
                                currentSize
                            )
                        }
                        notificationHelper.showDownload(data)
                    }
                }
            }
            handler.postDelayed(this, 1500)
        }
    }

    override fun onDestroy() {
        handler.removeCallbacks(downloadRunnable)
        super.onDestroy()
    }
}