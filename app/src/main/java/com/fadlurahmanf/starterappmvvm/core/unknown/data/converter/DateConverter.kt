package com.fadlurahmanf.starterappmvvm.core.unknown.data.converter

import androidx.room.TypeConverter
import com.fadlurahmanf.starterappmvvm.core.unknown.external.extension.formatDate5
import com.fadlurahmanf.starterappmvvm.core.unknown.external.extension.toDate1
import java.util.Date

class DateConverter {
    @TypeConverter
    fun toDateFromString(value: String?): Date? {
        return value?.toDate1()
    }

    @TypeConverter
    fun toStringFromDate(value: Date?): String? {
        return value?.formatDate5()
    }
}