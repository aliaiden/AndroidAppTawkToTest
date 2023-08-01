package com.alihaidertest.feature_github_users.domain.repository

import com.alihaidertest.feature_github_users.data.remote.dto.UserDto
import com.alihaidertest.feature_github_users.data.remote.dto.UserProfileDto
import com.alihaidertest.feature_github_users.domain.model.User
import com.alihaidertest.feature_github_users.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsers(): List<UserDto>
    suspend fun getPageUsers(pageID: Int): List<UserDto>
    fun getUsersBySearchKeyword(searchKey: String): List<UserDto>
    suspend fun getUserById(id: Int): UserDto
    suspend fun insertUser(user: UserDto)
    suspend fun insertUserNotes(id: Int, notes: String)
    suspend fun getUserByUsername(username: String): UserProfile
}