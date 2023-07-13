package com.fadlurahmanf.starterappmvvm.core.unknown.data.constant

object AppConstant {

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

    // RemoteConfig Key
    object RCK {
        const val TYPE_TOKEN = "TYPE_TOKEN"
    }

    // RemoteConfig Value
    object RCV {
        const val BASIC = "BASIC"
        const val GUEST = "GUEST"
    }
}