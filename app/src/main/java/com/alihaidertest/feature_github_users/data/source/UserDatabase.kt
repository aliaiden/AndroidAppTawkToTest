package com.alihaidertest.feature_github_users.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alihaidertest.feature_github_users.data.remote.dto.UserDto
import com.alihaidertest.feature_github_users.data.remote.dto.UserProfileDto

@Database(entities = [UserDto::class, UserProfileDto::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract val userDao: UserDao

    companion object {
        val DATABASE_NAME: String = "userdatabase"
    }
}

