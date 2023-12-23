package com.bigbigdw.manavara.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemBookMining
import com.bigbigdw.manavara.manavara.models.ItemAlert

@Database(entities = [ItemBookInfo::class], version = 1)
abstract class DBBookInfo: RoomDatabase() {
    abstract fun bookInfoDao(): DaoBookInfo
}

@Database(entities = [ItemBookMining::class], version = 1)
abstract class DBMining: RoomDatabase() {
    abstract fun miningDao(): DaoMining
}

@Database(entities = [ItemAlert::class], version = 1)
abstract class DBAlert: RoomDatabase() {
    abstract fun alertDao(): DaoAlert
}