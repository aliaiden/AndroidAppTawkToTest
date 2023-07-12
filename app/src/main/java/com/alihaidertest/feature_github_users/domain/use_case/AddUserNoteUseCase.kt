package com.alihaidertest.feature_github_users.domain.use_case

import com.alihaidertest.feature_github_users.domain.model.InvalidNotesException
import com.alihaidertest.feature_github_users.domain.repository.UserRepository
import javax.inject.Inject

class AddUserNoteUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend fun invoke(id: Int, notes: String){
        if (notes.isBlank()){
            throw InvalidNotesException("The user notes cannot be saved as empty")
        }
        repository.insertUserNotes(id, notes)
    }
}