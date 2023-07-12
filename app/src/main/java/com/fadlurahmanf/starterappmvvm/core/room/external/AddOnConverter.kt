package com.fadlurahmanf.starterappmvvm.core.room.external

import androidx.room.TypeConverter
import com.fadlurahmanf.starterappmvvm.core.room.data.dto.entity.AddOnExampleRoomEntity
import com.google.gson.Gson

class AddOnConverter {

    @TypeConverter
    fun fromAddOnToString(addOnConverter: AddOnExampleRoomEntity): String? {
        return try {
            return Gson().toJson(addOnConverter)
        } catch (_: Throwable) {
            null
        }
    }

    @TypeConverter
    fun fromStringToAddOn(value: String): AddOnExampleRoomEntity? {
        return try {
            return Gson().fromJson(value, AddOnExampleRoomEntity::class.java)
        } catch (_: Throwable) {
            null
        }
    }
}