package com.fadlurahmanf.starterappmvvm.core.data.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.fadlurahmanf.starterappmvvm.core.data.constant.SdfConstant
import com.fadlurahmanf.starterappmvvm.core.external.extension.formatDate5
import com.fadlurahmanf.starterappmvvm.core.external.extension.toDate1
import java.text.SimpleDateFormat
import java.util.Calendar
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