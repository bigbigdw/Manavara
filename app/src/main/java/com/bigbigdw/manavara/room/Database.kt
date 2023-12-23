package com.bigbigdw.manavara.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemBookMining

@Database(entities = [ItemBookInfo::class], version = 1)
abstract class DBBookInfo: RoomDatabase() {
    abstract fun bookInfoDao(): DaoBookInfo
}

@Database(entities = [ItemBookMining::class], version = 1)
abstract class DBMining: RoomDatabase() {
    abstract fun miningDao(): DaoMining
}