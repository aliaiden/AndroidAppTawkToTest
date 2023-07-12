package com.alihaidertest.feature_github_users.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alihaidertest.feature_github_users.data.remote.dto.UserDto
import com.alihaidertest.feature_github_users.domain.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserDto)

    @Query("SELECT * FROM UserDto WHERE pageID = :pageID")
    fun getPageUsers(pageID: Int): List<UserDto>

    @Query("SELECT * FROM UserDto")
    fun getUsers(): Flow<List<UserDto>>

    @Query("SELECT * FROM UserDto WHERE notes LIKE :key")
    fun getUsersBySearchKeyword(key: String): List<UserDto>

    @Query("UPDATE UserDto SET notes = :notes WHERE id = :id")
    suspend fun insertUserNotes(id: Int, notes: String)

    @Query("SELECT * FROM UserDto WHERE id = :id")
    fun getUserById(id: Int): UserDto





}
