package com.fadlurahmanf.starterappmvvm.core.room.data.dto.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fadlurahmanf.starterappmvvm.core.room.data.constant.RoomConstant

@Entity(tableName = RoomConstant.Table.example)
data class ExampleRoomEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "example_string")
    var exampleString: String? = null,
    @ColumnInfo(name = "add_on_string")
    var addOn: AddOnExampleRoomEntity? = null
)

data class AddOnExampleRoomEntity(
    var addOnString: String? = null
)
