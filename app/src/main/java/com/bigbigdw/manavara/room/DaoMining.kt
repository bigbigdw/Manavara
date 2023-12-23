package com.bigbigdw.manavara.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.bigbigdw.manavara.best.models.ItemBookMining

@Dao
interface DaoMining {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(itemBookInfoList : ItemBookMining)
}