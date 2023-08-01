package com.alihaidertest.feature_github_users.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alihaidertest.feature_github_users.data.remote.dto.UserDto
import com.alihaidertest.feature_github_users.domain.model.User
import com.alihaidertest.model.UsersResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserDto)

    @Query("SELECT * FROM UserDto WHERE pageID = :pageID")
    suspend fun getPageUsers(pageID: Int): List<UserDto>

    @Query("SELECT * FROM UserDto")
    fun getUsers(): Flow<List<UserDto>>

    @Query("SELECT * FROM UserDto WHERE notes LIKE :key")
    fun getUsersBySearchKeyword(key: String): List<UserDto>

    @Query("UPDATE UserDto SET notes = :notes WHERE id = :id")
    suspend fun insertUserNotes(id: Int, notes: String)

    @Query("SELECT * FROM UserDto WHERE id = :id")
    suspend fun getUserById(id: Int): UserDto

    @Query("SELECT * FROM UserDto WHERE id > :pageID AND id < :lastID")
    suspend fun getPageDataSet(pageID: Int, lastID: Int): List<UserDto>

    @Query("SELECT notes FROM UserDto WHERE id = :id")
    suspend fun getUserNotes(id: Int): String

}
