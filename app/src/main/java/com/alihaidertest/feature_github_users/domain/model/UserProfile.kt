package com.alihaidertest.feature_github_users.domain.model

data class UserProfile(
    val id: Int = -1,
    val avatar_url: String = "",
    val bio: String? = "",
    val created_at: String = "",
    val email: String? = "",
    val html_url: String = "",
    val location: String? = "",
    val login: String = "",
    val name: String = "",
    val twitter_username: String? = "",
    val type: String = "",
    val url: String = "",
    var notes: String = ""
)