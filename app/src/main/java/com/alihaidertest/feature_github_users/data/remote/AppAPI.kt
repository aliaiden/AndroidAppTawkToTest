package com.alihaidertest.feature_github_users.data.remote

import com.alihaidertest.feature_github_users.data.remote.dto.UserDto
import com.alihaidertest.feature_github_users.data.remote.dto.UserProfileDto
import com.alihaidertest.feature_github_users.domain.model.User
import com.alihaidertest.model.UsersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppAPI {
    @GET("users?since=0")
    suspend fun getUsers(): List<UserDto>

    @GET("users")
    suspend fun getGithubUsers(@Query("since") pageID: Int? = 0): List<UserDto>

    @GET("user/{username}")
    suspend fun getGithubUsers(@Path("username") username: String? = ""): UserProfileDto
}