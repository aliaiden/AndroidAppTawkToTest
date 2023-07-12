package com.alihaidertest.feature_github_users.presentation.user_profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alihaidertest.feature_github_users.common.Resource
import com.alihaidertest.feature_github_users.domain.model.InvalidNotesException
import com.alihaidertest.feature_github_users.domain.use_case.AddUserNoteUseCase
import com.alihaidertest.feature_github_users.domain.use_case.GetUserUseCase
import com.alihaidertest.feature_github_users.presentation.users.UsersState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val addUserNoteUseCase: AddUserNoteUseCase,
    saveStateHandle: SavedStateHandle //for getting navigation arguments
) : ViewModel() {

    //separate state for text field
    private val _note = mutableStateOf(
        NoteTextFieldState(
            hint = "Add note for this user..."
        )
    )
    val note: State<NoteTextFieldState> = _note

    private val _user = mutableStateOf(UserState())
    val user: State<UserState> = _user

    //event flow for preventing recreate UI when screen rotation is changed
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        saveStateHandle.get<Int>("id")?.let { userID ->
            viewModelScope.launch {
                getUserUseCase(id = userID)?.onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _user.value = UserState(
                                login = result.data!!.login,
                                avatar_url = result.data!!.avatar_url
                            )
                        }

                        is Resource.Error -> {
                            _user.value =
                                UserState(error = result.message ?: "Something went wrong!")
                        }

                        is Resource.Loading -> {
                            _user.value = UserState(isLoading = true)
                        }
//                    _user.value = user.value.copy(
//                        login = usr.login,
//                        avatar_url = usr.avatar_url
//                    )
//                    _note.value = note.value.copy(
//                        text = usr.notes,
//                        isHintVisible = false
//                    )
                    }
                }
            }
        }
    }

    fun onEvent(event: UserProfileEvent) {
        when (event) {
            is UserProfileEvent.EnteredNote -> {
                _note.value = note.value.copy(
                    text = event.value
                )
            }

            is UserProfileEvent.ChangeNoteFocus -> {
                _note.value = note.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            note.value.text.isBlank()
                )
            }

            is UserProfileEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        addUserNoteUseCase.invoke(
                            id = user.value.id,
                            notes = note.value.text
                        )
                        //emit so we can react to the events on UI screen
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNotesException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Something went wrong!"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}