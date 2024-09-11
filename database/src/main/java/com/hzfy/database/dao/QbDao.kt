package com.hzfy.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hzfy.database.entity.QbEntity

@Dao
interface QbDao {

    @Query("SELECT * from qb")
    fun selectAll() : List<QbEntity>


    @Query("SELECT * from qb WHERE url = :url")
    fun selectQbByUrl(url: String) : QbEntity?

    @Query("SELECT * from qb WHERE uid = :uid")
    fun selectQbByUid(uid: Int) : QbEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(qb: QbEntity): Long

    @Update
    fun update(qb: QbEntity): Int

    @Delete
    fun delete(qb: QbEntity): Int
}