package com.fadlurahmanf.starterappmvvm.core.logger.data.dto.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fadlurahmanf.starterappmvvm.core.room.data.constant.RoomConstant
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.AppConstant
import java.util.Date

@Entity(tableName = RoomConstant.Table.logger)
data class LoggerEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var type: String? = null,
    var date: Date? = null,
    var message: String? = null,
)