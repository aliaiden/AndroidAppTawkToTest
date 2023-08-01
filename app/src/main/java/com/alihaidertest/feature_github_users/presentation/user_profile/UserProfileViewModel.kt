package com.alihaidertest.feature_github_users.presentation.user_profile

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alihaidertest.feature_github_users.common.Resource
import com.alihaidertest.feature_github_users.domain.model.InvalidNotesException
import com.alihaidertest.feature_github_users.domain.use_case.AddUserNoteUseCase
import com.alihaidertest.feature_github_users.domain.use_case.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val addUserNoteUseCase: AddUserNoteUseCase,
    saveStateHandle: SavedStateHandle //for getting navigation arguments
) : ViewModel() {

    private val _user = MutableStateFlow(UserState())
    val user: StateFlow<UserState> = _user

    //event flow for preventing recreate UI when screen rotation is changed
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        saveStateHandle.get<String>("username")?.let { username ->
            viewModelScope.launch {
                getUserUseCase(username = username)?.collect() { result ->
                    when (result) {
                        is Resource.Success -> {
                            _user.value = UserState(
                                id = result.data!!.id,
                                login = result.data!!.login,
                                avatar_url = result.data!!.avatar_url,
                                type = result.data!!.type,
                                html_url = result.data!!.html_url,
                                notes = result.data!!.notes
                            )
                        }

                        is Resource.Error -> {
                            _user.value =
                                UserState(error = result.message ?: "Something went wrong!")
                        }

                        is Resource.Loading -> {
                            _user.value = UserState(isLoading = true)
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: UserProfileEvent) {
        when (event) {

            is UserProfileEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        addUserNoteUseCase(
                            id = user.value.id,
                            notes = user.value.notes
                        )?.collect() { result ->
                            when (result) {
                                is Resource.Success -> {
                                    // go back
//                                    _user.value = UserState(notes = user.value.notes)
                                    Log.d("save notes", "success")
                                }

                                is Resource.Error -> {
                                }

                                is Resource.Loading -> {
//                                    _user.value = UserState(isLoading = true)
                                }
                            }
                        }
                        //emit so we can react to the events on UI screen
//                        _eventFlow.emit(UiEvent.SaveNote)
                        onEvent(UserProfileEvent.SavedNote)

                    } catch (e: InvalidNotesException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Something went wrong!"
                            )
                        )
                    }
                }
            }

            is UserProfileEvent.SavedNote -> {
                // go back
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ShowSnackbar("Notes saved for ${user.value.login} successfully"))
                }
            }

            is UserProfileEvent.EmptyNoteEntered -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ShowSnackbar("Please add notes first"))
                }

            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
    }

    fun onSaveClick() {
        if (user.value.notes.isBlank()) {
            onEvent(UserProfileEvent.EmptyNoteEntered)
        } else {
            onEvent(UserProfileEvent.SaveNote)
        }
    }

}