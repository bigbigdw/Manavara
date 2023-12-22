package com.bigbigdw.manavara.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bigbigdw.manavara.best.models.ItemBookInfo

@Dao
interface DaoBookInfo {

    @Query("SELECT * FROM ItemBookInfo WHERE bookCode = :bookCode")
    fun getRank(bookCode: String): List<ItemBookInfo>

    @Query("SELECT number FROM ItemBookInfo WHERE platform = :platform AND title = :title GROUP BY platform")
    fun findName(platform: String, title: String): Int

    @Query("SELECT * FROM ItemBookInfo WHERE platform = :platform AND title = :title")
    fun findDay(platform: String, title: String): ItemBookInfo

    @Query("SELECT * FROM ItemBookInfo WHERE platform = :platform AND title = :title AND number = :number")
    fun findDay(platform: String, title: String , number: Int): ItemBookInfo

    @Query("SELECT * FROM ItemBookInfo")
    fun getAll(): List<ItemBookInfo>

    @Query("DELETE FROM ItemBookInfo WHERE platform = :platform")
    fun initWeek(platform: String)

    @Query("DELETE FROM ItemBookInfo WHERE bookCode = :bookCode")
    fun deleteItem(bookCode: String)

    @Query("SELECT COUNT(title) FROM ItemBookInfo WHERE title =:title")
    fun countTrophy(title: String): Int

    @Query("DELETE FROM ItemBookInfo")
    fun initAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(itemBookInfoList : ArrayList<ItemBookInfo>)

    @Delete
    fun delete(user: ItemBookInfo)

    @Update
    fun update(user: ItemBookInfo)
}