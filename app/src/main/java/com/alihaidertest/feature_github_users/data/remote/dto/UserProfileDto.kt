package com.alihaidertest.feature_github_users.data.remote.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alihaidertest.feature_github_users.domain.model.UserProfile

@Entity
data class UserProfileDto(
    @PrimaryKey val id: Int,
    val login: String,
    val avatar_url: String,
    val bio: String,
    val blog: String,
    val company: String,
    val created_at: String,
    val email: String,
    val events_url: String,
    val followers: Int,
    val followers_url: String,
    val following: Int,
    val following_url: String,
    val gists_url: String,
    val gravatar_id: String,
    val hireable: String,
    val html_url: String,
    val location: String,
    val name: String,
    val node_id: String,
    val organizations_url: String,
    val public_gists: Int,
    val public_repos: Int,
    val received_events_url: String,
    val repos_url: String,
    val site_admin: Boolean,
    val starred_url: String,
    val subscriptions_url: String,
    val twitter_username: String,
    val type: String,
    val updated_at: String,
    val url: String,
    val pageID: Int = -1,
) {
    fun toUserDto(): UserDto {
        return UserDto(
            id = id,
            avatar_url = avatar_url,
            events_url = events_url,
            followers_url = followers_url,
            following_url = following_url,
            gists_url = gists_url,
            gravatar_id = gravatar_id,
            html_url = html_url,
            login = login,
            node_id = node_id,
            notes = "",
            type = type,
            pageID = pageID,
            organizations_url = organizations_url,
            received_events_url = received_events_url,
            repos_url = repos_url,
            site_admin = site_admin,
            starred_url = starred_url,
            subscriptions_url = subscriptions_url,
            url = url
        )
    }
}

fun UserProfileDto.toUserProfile(): UserProfile {
    return UserProfile(
        id = id,
        created_at = created_at,
        login = login,
        type = type,
        html_url = html_url,
        name = name,
        email = email,
        bio = bio,
        avatar_url = avatar_url,
        location = location,
        twitter_username = twitter_username,
        url = url
    )
}