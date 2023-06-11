package com.alihaidertest.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alihaidertest.model.UsersResponse

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(user: UsersResponse)

    @Query("SELECT * FROM UsersResponse WHERE id > :pageID AND id < :lastID")
    fun getPageDataSet(pageID: Int, lastID: Int): List<UsersResponse>

    @Query("SELECT * FROM UsersResponse")
    fun getAllDataSet(): List<UsersResponse>

    @Query("UPDATE UsersResponse SET notes = :notes WHERE id = :id")
    fun insertUserNotes(notes: String, id: Int)

    @Query("SELECT * FROM UsersResponse WHERE id = :id")
    fun getUser(id: Int): UsersResponse





}
