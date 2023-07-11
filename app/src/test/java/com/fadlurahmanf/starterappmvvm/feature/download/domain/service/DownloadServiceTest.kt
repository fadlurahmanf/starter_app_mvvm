package com.fadlurahmanf.starterappmvvm.feature.download.domain.service

import android.content.Context
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class DownloadServiceTest{
    lateinit var downloadService: DownloadService

    @Before
    fun before(){
        downloadService = DownloadService()
    }

    @Test
    fun tes(){
        val context = mock<Context>()
        DownloadService.startService(
            context,
            "url",
            "tes.jpg"
        )
    }
}