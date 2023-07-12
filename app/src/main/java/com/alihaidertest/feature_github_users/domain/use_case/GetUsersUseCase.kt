package com.alihaidertest.feature_github_users.domain.use_case

import android.util.Log
import com.alihaidertest.feature_github_users.common.Resource
import com.alihaidertest.feature_github_users.data.remote.dto.toUser
import com.alihaidertest.feature_github_users.domain.model.User
import com.alihaidertest.feature_github_users.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(pageID: Int = -1): Flow<Resource<List<User>>> = flow {
        try {
            Log.d("users", "loading")

//            emit(Resource.Loading())
//            val users = repository.getPageUsers(pageID).map { it.toUser() }
            val users = repository.getUsers().map { it.toUser() }
            Log.d("users", users.toString())
            emit(Resource.Success(users))
        } catch (e: HttpException){

        } catch (e: IOException){

        }
    }
}