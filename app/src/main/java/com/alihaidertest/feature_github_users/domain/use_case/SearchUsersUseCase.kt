package com.alihaidertest.feature_github_users.domain.use_case

import com.alihaidertest.feature_github_users.data.remote.dto.UserDto
import com.alihaidertest.feature_github_users.domain.model.User
import com.alihaidertest.feature_github_users.domain.repository.UserRepository
import com.alihaidertest.feature_github_users.domain.util.UserListType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(searchKey: String): List<UserDto> {
        return repository.getUsersBySearchKeyword(searchKey)
    }
}