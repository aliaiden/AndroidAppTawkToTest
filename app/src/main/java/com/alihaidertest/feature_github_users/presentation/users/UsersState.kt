package com.alihaidertest.feature_github_users.presentation.users

import com.alihaidertest.feature_github_users.domain.model.User
import com.alihaidertest.feature_github_users.domain.util.UserListType

data class UsersState(
    val isLoading: Boolean = false,
    val error: String = "",
    val users: List<User> = emptyList(),
    val listType: UserListType = UserListType.Normal(0),
    val pageID: Int = 0,
    val searchKey: String = ""
)
