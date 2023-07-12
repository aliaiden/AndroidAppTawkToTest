package com.alihaidertest.feature_github_users.domain.use_case

import com.alihaidertest.feature_github_users.common.Resource
import com.alihaidertest.feature_github_users.data.remote.dto.toUser
import com.alihaidertest.feature_github_users.domain.model.User
import com.alihaidertest.feature_github_users.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(id: Int): Flow<Resource<User>>? = flow {
        try {
            emit(Resource.Loading())
            val user = repository.getUserById(id).toUser()
            emit(Resource.Success(user))
        } catch (e: HttpException){

        } catch (e: IOException){

        }
    }
}