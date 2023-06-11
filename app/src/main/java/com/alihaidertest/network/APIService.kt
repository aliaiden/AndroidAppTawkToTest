package com.alihaidertest.network

import com.alihaidertest.model.UsersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("users")
    suspend fun getGithubUsers(@Query("since") pageID: Int? = 0): List<UsersResponse>
}
