package com.bigbigdw.manavara.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bigbigdw.manavara.room.DaoRoom
import com.bigbigdw.manavara.room.RoomBookListDataBest

@Database(entities = [RoomBookListDataBest::class], version = 4)
abstract class DBRoom: RoomDatabase() {
    abstract fun roomdao(): DaoRoom
}