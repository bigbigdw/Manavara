package com.bigbigdw.manavara.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemBookMining
import com.bigbigdw.manavara.manavara.models.ItemAlert

@Dao
interface DaoBookInfo {

    @Query("SELECT * FROM ItemBookInfo")
    fun getAll(): List<ItemBookInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(itemBookInfoList : ArrayList<ItemBookInfo>)

    @Delete
    fun delete(user: ItemBookInfo)
}

@Dao
interface DaoMining {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(itemBookInfoList : ItemBookMining)

    @Query("SELECT * FROM ItemBookMining")
    fun getAll(): List<ItemBookMining>
}

@Dao
interface DaoAlert {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(itemAlert : ItemAlert)

    @Query("SELECT * FROM ItemAlert")
    fun getAll(): List<ItemAlert>
}