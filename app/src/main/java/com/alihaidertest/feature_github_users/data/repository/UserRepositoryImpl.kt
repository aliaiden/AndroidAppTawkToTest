package com.alihaidertest.feature_github_users.data.repository

import android.util.Log
import com.alihaidertest.MainApplication
import com.alihaidertest.db.getDatabase
import com.alihaidertest.feature_github_users.data.remote.AppAPI
import com.alihaidertest.feature_github_users.data.remote.dto.UserDto
import com.alihaidertest.feature_github_users.data.remote.dto.toUser
import com.alihaidertest.feature_github_users.data.source.UserDao
import com.alihaidertest.feature_github_users.domain.model.User
import com.alihaidertest.feature_github_users.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import java.net.UnknownHostException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: AppAPI,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getUsers(): List<UserDto> {
        return api.getUsers()
    }

    override suspend fun getPageUsers(pageID: Int): List<UserDto> {
        return saveToLocalDBAndReturn(getFromRemote(pageID), pageID)
    }

    private fun saveToLocalDBAndReturn(
        remoteUsersData: List<UserDto>,
        pageID: Int?
    ): List<UserDto> {
        if (remoteUsersData.isNotEmpty()) {
            // save into local db
            remoteUsersData.forEach { item ->
                item.pageID = pageID!!
                if (item.notes.isNullOrEmpty()) item.notes = ""

                insertUser(item)
            }
        }

        //load from local
        var lastID = pageID?.plus(30)
        if (remoteUsersData.isNotEmpty()) lastID = remoteUsersData[remoteUsersData.lastIndex].id
        val loadFromLocal =
//            userDao.getPageDataSet(pageID!!, lastID!!)
            userDao.getPageUsers(pageID!!)
//            getDatabase(MainApplication.context).appDao().getAllDataSet()
        Log.d("FetchUsers from local db", loadFromLocal.toString())
        return loadFromLocal
    }

    private suspend fun getFromRemote(pageID: Int): List<UserDto> {
        lateinit var remoteUsersData : List<UserDto>
        try {
            remoteUsersData = api.getGithubUsers(pageID)

            print("REMOTE DATA" + remoteUsersData)
            Log.d("FetchUsers from api", remoteUsersData.toString())
        } catch (e: UnknownHostException) {
        }
        return remoteUsersData
    }

    override fun getUsersBySearchKeyword(key: String): List<UserDto> {
        return userDao.getUsersBySearchKeyword(key)
    }

    override suspend fun getUserById(id: Int): UserDto {
        return userDao.getUserById(id)
    }

    override fun insertUser(user: UserDto) {
        return userDao.insertUser(user)
    }

    override suspend fun insertUserNotes(id: Int, notes: String) {
        return userDao.insertUserNotes(id, notes)
    }

}