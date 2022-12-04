package com.fadlurahmanf.starterappmvvm.dto.response.example

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fadlurahmanf.starterappmvvm.constant.RoomKey
import com.google.gson.annotations.SerializedName

data class SurahsResponse(
    @SerializedName("surahs")
    var surahs:List<SurahResponse>
)

@Entity(tableName = RoomKey.surahTable)
data class SurahResponse(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("number")
    var number:Int?=null,
    @SerializedName("name")
    var name:String?=null,
)
