package com.alihaidertest.network

import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        private val BASE_URL = "https://api.github.com/"

        private val retrofit: Retrofit by lazy {
            val dispatcher = Dispatcher()
            dispatcher.maxRequests = 1

            val client = OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val apiService: APIService by lazy {
            retrofit.create(APIService::class.java)
        }
    }
}
