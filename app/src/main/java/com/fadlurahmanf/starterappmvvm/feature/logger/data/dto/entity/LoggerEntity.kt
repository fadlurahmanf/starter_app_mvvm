package com.fadlurahmanf.starterappmvvm.feature.logger.data.dto.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fadlurahmanf.starterappmvvm.core.data.constant.AppKey

@Entity(tableName = AppKey.RoomTable.logger)
data class LoggerEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var type: String? = null,
    var date: String? = null,
    var message: String? = null,
)
