package com.fadlurahmanf.starterappmvvm.unknown.dto.response.example

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fadlurahmanf.starterappmvvm.core.room.data.constant.RoomConstant
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.AppConstant
import com.google.gson.annotations.SerializedName

data class SurahsResponse(
    @SerializedName("surahs")
    var surahs:List<SurahResponse>
)

@Entity(tableName = RoomConstant.Table.surah)
data class SurahResponse(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("number")
    var number:Int?=null,
    @SerializedName("name")
    var name:String?=null,
)
