package com.alihaidertest.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alihaidertest.model.UsersResponse
import com.alihaidertest.repo.GithubRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList
import java.util.Locale

class MainActivityViewModel : ViewModel() {
    private val repository = GithubRepository()

    private val _users = MutableLiveData<List<UsersResponse>>()
    private val _pageID = MutableLiveData(0)
    val users: LiveData<List<UsersResponse>> = _users

    private val _user = MutableLiveData<UsersResponse>()
    val user: LiveData<UsersResponse> = _user

    fun fetchUsers() {
        viewModelScope.launch {
            try {
                val users = repository.getGithubUsers(_pageID.value)
                if (_pageID.value == 0) _users.value = users
                else _users.value = _users.value.orEmpty() + users.orEmpty()

                Log.d("FetchUsers", _users.value.toString())
                _pageID.value = _users.value!![_users.value!!.lastIndex].id
            } catch (e: Exception) {
                // Handle error
                Log.d("FetchUsers error", e.message.toString())
            }
        }
    }

    fun fetchUser(id: Int) {
        viewModelScope.launch {
            try {
                val user = repository.getGithubUser(id)
                Log.d("FetchUser", _user.value.toString())
                _user.value = user
            } catch (e: Exception) {
                Log.d("FetchUser error", e.message.toString())
            }
        }
    }

    fun performQuery(query: String) {
        val filteredList = _users.value
            ?.filter {
                it.html_url.lowercase(Locale.getDefault()).contains(
                    query.lowercase(Locale.getDefault())
                )
            }
        _users.value = filteredList
    }

    fun insertUserNotes(notes: String, id: Int) {
        viewModelScope.launch {
            repository.insertUserNotes(notes, id)
        }
    }
}
