package com.alihaidertest.feature_github_users.presentation.users

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alihaidertest.feature_github_users.common.ConnectivityObserver
import com.alihaidertest.feature_github_users.common.Resource
import com.alihaidertest.feature_github_users.domain.use_case.GetUsersUseCase
import com.alihaidertest.feature_github_users.domain.use_case.SearchUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val searchUsersUseCase: SearchUsersUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _state = mutableStateOf(UsersState())
    val state: State<UsersState> = _state

    //keep track of coroutine that gets users from repository
    //so we can cancel existing coroutine when fetching users again
    private var getUsersJob: Job? = null

    //first time screen is opened
    init {
        getConnectivity()
        getUsers(0)
    }

    fun onEvent(event: UsersEvent) {
        //anything user can do on users screen
        when (event) {
            is UsersEvent.GetUsers -> {
                // if list of users already loaded
                if (state.value.pageID == event.pageID) {
                    return
                }
                getUsers(event.pageID)

            }

            is UsersEvent.SearchUsers -> {
                _state.value = state.value.copy(searchKey = state.value.searchKey)
                viewModelScope.launch {
                    searchUsersUseCase(event.searchKey)
                }
            }
        }
    }

    fun getUsers(pageID: Int) {
        getUsersJob?.cancel()

        viewModelScope.launch {
            getUsersUseCase(pageID)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value =
                                UsersState(users = _state.value.users + result.data.orEmpty())
                        }

                        is Resource.Error -> {
                            _state.value =
                                UsersState(error = result.message ?: "Something went wrong!")
                        }

                        is Resource.Loading -> {
                            if (pageID == 0)
                                _state.value = UsersState(isLoading = true)
                        }
                    }
                }
        }
    }

    fun getConnectivity() {
        viewModelScope.launch {
            connectivityObserver.observe().collect() { result ->
                _state.value = UsersState(networkStatus = "Internet Connection: ${result.name}")
//                    ConnectivityObserver.Status.Unavailable -> {
//
//                    }
//                    ConnectivityObserver.Status.Available -> {
//
//                    }
//                    ConnectivityObserver.Status.Lost -> {
//
//                    }
            }
        }
    }

}