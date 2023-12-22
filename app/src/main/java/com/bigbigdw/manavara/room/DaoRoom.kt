package com.bigbigdw.manavara.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DaoRoom {
    @Query("DELETE FROM RoomBookListDataBest")
    fun initAll()
}