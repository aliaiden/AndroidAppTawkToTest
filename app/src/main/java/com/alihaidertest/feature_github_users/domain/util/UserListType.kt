package com.alihaidertest.feature_github_users.domain.util

sealed class UserListType {
    constructor(pageID: Int)
    constructor(key: String)

    class Normal(pageID: Int): UserListType(pageID)
    class Search(key: String): UserListType(key)
}