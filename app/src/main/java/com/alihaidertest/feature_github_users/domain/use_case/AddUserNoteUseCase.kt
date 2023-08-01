package com.alihaidertest.feature_github_users.domain.use_case

import com.alihaidertest.feature_github_users.common.Resource
import com.alihaidertest.feature_github_users.domain.model.InvalidNotesException
import com.alihaidertest.feature_github_users.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddUserNoteUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(id: Int, notes: String): Flow<Resource<Boolean>>? = flow {
        emit(Resource.Loading())
        if (notes.isBlank()){
            emit(Resource.Success(false))
            throw InvalidNotesException("The user notes cannot be saved as empty")
        }
        repository.insertUserNotes(id, notes)
        emit(Resource.Success(true))
    }
}