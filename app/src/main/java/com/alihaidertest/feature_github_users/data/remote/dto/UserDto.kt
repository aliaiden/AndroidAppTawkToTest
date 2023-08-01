package com.alihaidertest.feature_github_users.data.remote.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alihaidertest.feature_github_users.domain.model.User
import com.alihaidertest.feature_github_users.domain.model.UserProfile

@Entity
data class UserDto(
    @PrimaryKey
    val id: Int,
    val avatar_url: String,
    val events_url: String,
    val followers_url: String,
    val following_url: String,
    val gists_url: String,
    val gravatar_id: String,
    val html_url: String,
    val login: String,
    val node_id: String,
    val organizations_url: String,
    val received_events_url: String,
    val repos_url: String,
    val site_admin: Boolean,
    val starred_url: String,
    val subscriptions_url: String,
    val type: String,
    val url: String,
    var notes: String = "",
    var pageID: Int
) {
    fun toUserProfile(): UserProfile {
        return UserProfile(
            id = id,
            created_at = "",
            login = login,
            type = type,
            html_url = html_url,
            name = "",
            email = "",
            bio = "",
            avatar_url = avatar_url,
            location = "",
            twitter_username = "",
            url = url,
            notes = notes
        )
    }
}

fun UserDto.toUser(): User {
    return User(
        id = id,
        login = login,
        html_url = html_url,
        url = url,
        type = type,
        notes = "",
        avatar_url = avatar_url,
        pageID = pageID,
    )
}
