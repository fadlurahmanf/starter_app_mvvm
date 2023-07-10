package com.fadlurahmanf.starterappmvvm.feature.download.domain.usecases

import android.content.Context
import com.fadlurahmanf.starterappmvvm.feature.download.domain.service.DownloadService
import javax.inject.Inject

class DownloadImpl @Inject constructor(
    private val context: Context
) {
    fun startDownload(url: String, fileName: String) {
        DownloadService.startService(
            context, url, fileName
        )
    }
}