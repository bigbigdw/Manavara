package com.bigbigdw.manavara.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bigbigdw.manavara.best.models.ItemBookInfo

@Database(entities = [ItemBookInfo::class], version = 1)
abstract class DBBookInfo: RoomDatabase() {
    abstract fun bookInfoDao(): DaoBookInfo
}