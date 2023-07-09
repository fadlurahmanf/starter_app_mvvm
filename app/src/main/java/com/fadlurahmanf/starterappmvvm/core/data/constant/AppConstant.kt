package com.fadlurahmanf.starterappmvvm.core.data.constant

object AppConstant {
    object RoomDB {
        const val core = "core_db"
    }

    object RoomTable {
        const val surah = "surah_table"
        const val logger = "logger_table"
    }

    object Notification {
        const val GENERAL_CHANNEL_ID = "GENERAL"
        const val GENERAL_CHANNEL = "Umum"
        const val GENERAL_DESCRIPTION = "Notifikasi umum"

        const val DOWNLOAD_CHANNEL_ID = "DOWNLOAD"
        const val DOWNLOAD_CHANNEL = "Unduh"
        const val DOWNLOAD_DESCRIPTION = "Notifikasi unduh"

        const val DEFAULT_NOTIFICATION_ID = 0
        const val DEFAULT_DOWNLOAD_NOTIFICATION_ID = 1
    }
}