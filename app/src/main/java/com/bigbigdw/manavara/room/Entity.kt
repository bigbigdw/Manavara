package com.bigbigdw.manavara.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomBookListDataBest (
    var writer: String = "",
    var title: String = "",
    var bookImg: String = "",
    var bookCode: String = "",
    var info1: String = "",
    var info2: String = "",
    var info3: String = "",
    var info4: String = "",
    var info5: String = "",
    var info6: String = "",
    var number: Int = 0,
    var date: String = "",
    var type: String = "",
    var memo: String = "",
    var week: Int = 0,
    var month: Int = 0,
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}