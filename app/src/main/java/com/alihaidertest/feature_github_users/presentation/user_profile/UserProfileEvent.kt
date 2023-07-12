package com.alihaidertest.feature_github_users.presentation.user_profile

import androidx.compose.ui.focus.FocusState

sealed class UserProfileEvent{
    data class EnteredNote(val value: String): UserProfileEvent()
    data class ChangeNoteFocus(val focusState: FocusState): UserProfileEvent()
    object SaveNote: UserProfileEvent()
}
