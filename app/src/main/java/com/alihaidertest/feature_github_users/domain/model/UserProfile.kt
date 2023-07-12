package com.alihaidertest.feature_github_users.domain.model

data class UserProfile(
    val id: Int,
    val avatar_url: String,
    val bio: Any,
    val created_at: String,
    val email: Any,
    val html_url: String,
    val location: Any,
    val login: String,
    val name: String,
    val twitter_username: String,
    val type: String,
    val url: String
)