package com.alihaidertest.feature_github_users.presentation.users

import com.alihaidertest.feature_github_users.domain.util.UserListType

sealed class UsersEvent{
    data class GetUsers(val pageID: Int): UsersEvent()
    data class SearchUsers(val searchKey: String): UsersEvent()
}
