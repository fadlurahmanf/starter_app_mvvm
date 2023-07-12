package com.fadlurahmanf.starterappmvvm.feature.download.external.helper

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.unknown.data.dto.exception.CustomException
import java.io.File

class DownloadHelper(
    private val applicationContext: Context
) {
    private var downloadManager: DownloadManager =
        applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    private var notificationImpl: DownloadNotificationHelper =
        DownloadNotificationHelper(applicationContext)

    fun getDownloadId(urlDownload: String, downloadFileName: String): Long {
        if (urlDownload.isEmpty() || downloadFileName.isEmpty()) {
            throw CustomException(
                idRawMessage = R.string.download_url_not_valid_desc
            )
        }
        val file =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path)
        val downloadRequest = DownloadManager.Request(Uri.parse(urlDownload))
        downloadRequest.apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        }
        val uri =
            Uri.withAppendedPath(Uri.fromFile(file), downloadFileName)
        downloadRequest.setDestinationUri(uri)
        return downloadManager.enqueue(downloadRequest)
    }

    fun updateProgressDownload(downloadId: Long): Int {
        val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))
        if (cursor.moveToFirst()) {
            val status: Int = getStatus(cursor)
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                return DownloadManager.STATUS_SUCCESSFUL
            } else if (status == DownloadManager.STATUS_FAILED) {
                throw CustomException(
                    idRawMessage = R.string.download_failed_desc
                )
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
        return DownloadManager.STATUS_RUNNING
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
}