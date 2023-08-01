package com.alihaidertest.feature_github_users.data.repository

import android.util.Log
import com.alihaidertest.feature_github_users.data.remote.AppAPI
import com.alihaidertest.feature_github_users.data.remote.dto.UserDto
import com.alihaidertest.feature_github_users.data.source.UserDao
import com.alihaidertest.feature_github_users.domain.model.UserProfile
import com.alihaidertest.feature_github_users.domain.repository.UserRepository
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
        var sinceID = 0
        if (pageID > 0) {
            var currentPageUsers = userDao.getPageUsers(pageID - 1)
            sinceID = currentPageUsers[currentPageUsers.lastIndex].id
        }
        return saveToLocalDBAndReturn(getFromRemote(sinceID), pageID)
    }

    private suspend fun saveToLocalDBAndReturn(
        remoteUsersData: List<UserDto>,
        pageID: Int?
    ): List<UserDto> {
        if (remoteUsersData.isNotEmpty()) {
            // save into local db
            remoteUsersData.forEach { item ->
                item.pageID = pageID!!
                val savedNotes = userDao.getUserNotes(item.id)
                if (savedNotes.isNullOrEmpty()) item.notes = "" else item.notes = savedNotes

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

    private suspend fun getFromRemote(sinceID: Int): List<UserDto> {
        var remoteUsersData: List<UserDto> = emptyList()
        try {
            remoteUsersData = api.getGithubUsers(sinceID)

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

    override suspend fun insertUser(user: UserDto) {
        return userDao.insertUser(user)
    }

    override suspend fun insertUserNotes(id: Int, notes: String) {
        return userDao.insertUserNotes(id, notes)
    }

    override suspend fun getUserByUsername(username: String): UserProfile {
        var user = UserProfile()

        try {
            var remoteUser = api.getGithubUser(username)

            print("REMOTE USER" + remoteUser)
            Log.d("FetchUserProfile from api", remoteUser.toString())

            val userNotes = userDao.getUserNotes(remoteUser.id)

            // save into local db
            insertUser(remoteUser.toUserDto())

            // ffetch user from local db

            user = userDao.getUserById(remoteUser.id).toUserProfile()
            user.notes = userNotes

        } catch (e: UnknownHostException) {
        }
        return user
    }
}