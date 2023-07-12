package com.alihaidertest.feature_github_users.presentation.util

sealed class Screen(val route: String) {
    object UsersScreen: Screen("users_screen")
    object UserProfileScreen: Screen("user_profile_screen")
}