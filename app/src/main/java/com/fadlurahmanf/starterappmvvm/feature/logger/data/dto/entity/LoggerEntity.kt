package com.fadlurahmanf.starterappmvvm.feature.logger.data.dto.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.fadlurahmanf.starterappmvvm.core.data.constant.AppConstant
import com.fadlurahmanf.starterappmvvm.core.data.converter.DateConverter
import java.util.Date

@Entity(tableName = AppConstant.RoomTable.logger)
data class LoggerEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var type: String? = null,
    var date: Date? = null,
    var message: String? = null,
)
