package com.alihaidertest.repo

import android.util.Log
import com.alihaidertest.MainApplication
import com.alihaidertest.db.getDatabase
import com.alihaidertest.model.UsersResponse
import com.alihaidertest.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class GithubRepository {
    private val apiService = RetrofitInstance.apiService

    suspend fun getGithubUsers(pageID: Int?): List<UsersResponse>? = withContext(Dispatchers.IO) {
        var remoteUsersData: List<UsersResponse> = ArrayList()
        try {
            remoteUsersData = apiService.getGithubUsers(pageID)
        } catch (e: UnknownHostException) {
        }
        print(remoteUsersData)
        Log.d("FetchUsers from api", remoteUsersData.toString())


        if (remoteUsersData.isNotEmpty()) {
            // save into local db
            remoteUsersData.forEach { item ->
                item.pageID = pageID!!
                if (item.notes.isNullOrEmpty()) item.notes = ""

                insertData(item)
            }
        }

        //load from local
        var lastID = pageID?.plus(30)
        if (remoteUsersData.isNotEmpty()) lastID = remoteUsersData[remoteUsersData.lastIndex].id
        val loadFromLocal =
            getDatabase(MainApplication.context).appDao().getPageDataSet(pageID!!, lastID!!)
//            getDatabase(MainApplication.context).appDao().getAllDataSet()
        Log.d("FetchUsers from local db", loadFromLocal.toString())

        return@withContext loadFromLocal
    }

    fun insertData(model: UsersResponse) {
        getDatabase(MainApplication.context).appDao().insertItems(model)
    }

    suspend fun insertUserNotes(notes: String, id: Int) = withContext(Dispatchers.IO) {
        getDatabase(MainApplication.context).appDao().insertUserNotes(notes, id)

    }

    suspend fun getGithubUser(id: Int): UsersResponse? = withContext(Dispatchers.IO) {
        val loadFromLocal = getDatabase(MainApplication.context).appDao().getUser(id!!)
        Log.d("FetchUser from local db", loadFromLocal.toString())

        return@withContext loadFromLocal
    }
//    suspend fun getGithubUserProfile(): UsersProfileResponse {
//        return apiService.getAPIService()
//    }

}
