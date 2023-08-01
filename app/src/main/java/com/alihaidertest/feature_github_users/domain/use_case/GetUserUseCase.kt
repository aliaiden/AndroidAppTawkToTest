package com.alihaidertest.feature_github_users.domain.use_case

import com.alihaidertest.feature_github_users.common.Resource
import com.alihaidertest.feature_github_users.domain.model.UserProfile
import com.alihaidertest.feature_github_users.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(username: String): Flow<Resource<UserProfile>>? = flow {
        try {
            emit(Resource.Loading())
            val user = repository.getUserByUsername(username)
            emit(Resource.Success(user))
        } catch (e: HttpException){
            emit(Resource.Error("Something went wrong", UserProfile()))
        } catch (e: IOException){
            emit(Resource.Error("Not able to reach remote server", UserProfile()))

        }
    }
}