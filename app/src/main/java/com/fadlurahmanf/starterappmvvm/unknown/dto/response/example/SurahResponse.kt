package com.fadlurahmanf.starterappmvvm.unknown.dto.response.example

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fadlurahmanf.starterappmvvm.core.data.constant.AppKey
import com.google.gson.annotations.SerializedName

data class SurahsResponse(
    @SerializedName("surahs")
    var surahs:List<SurahResponse>
)

@Entity(tableName = AppKey.RoomTable.surah)
data class SurahResponse(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("number")
    var number:Int?=null,
    @SerializedName("name")
    var name:String?=null,
)
