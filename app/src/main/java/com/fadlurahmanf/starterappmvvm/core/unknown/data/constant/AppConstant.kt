package com.fadlurahmanf.starterappmvvm.core.unknown.data.constant

object AppConstant {
    object RoomDB {
        const val core = "core_db"
        const val logger = "logger_db"
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

    // shared preference
    object Sp {
        const val SP_KEY = "SP_KEY"
        const val LANGUAGE_CODE = "LANGUAGE_CODE"

        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"

        const val PRIVATE_KEY = "PRIVATE_KEY"
        const val PUBLIC_KEY = "PUBLIC_KEY"
    }
}