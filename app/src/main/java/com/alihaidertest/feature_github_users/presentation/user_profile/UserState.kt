package com.alihaidertest.feature_github_users.presentation.user_profile

data class UserState(
    val id: Int = -1,
    val login: String = "",
    val node_id: String = "",
    val avatar_url: String = "",
    val gravatar_id: String = "",
    val url: String = "",
    val html_url: String = "",
    val followers_url: String = "",
    val following_url: String = "",
    val gists_url: String = "",
    val starred_url: String = "",
    val subscriptions_url: String = "",
    val organizations_url: String = "",
    val repos_url: String = "",
    val events_url: String = "",
    val received_events_url: String = "",
    val type: String = "",
    val site_admin: Boolean = false,
    var pageID: Int = -1,
    var notes: String = "",
    val error: String = "",
    val isLoading: Boolean = false
)
