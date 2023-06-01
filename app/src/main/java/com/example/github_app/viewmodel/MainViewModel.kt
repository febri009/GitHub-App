package com.example.github_app.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.github_app.model.Users
import com.example.github_app.State
import com.example.github_app.State.Companion.DATA_NULL
import com.example.github_app.State.Companion.NO_RESPON
import com.example.github_app.api.Client
import com.example.github_app.model.UserResponse
import com.example.github_app.preference.Setting
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope

class MainViewModel: ViewModel() {

    private val _userList = MutableLiveData<List<Users?>?>()
    val userList: LiveData<List<Users?>?> = _userList

    val searchQuery = MutableLiveData<String>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<State<String>>()
    val snackbarText: LiveData<State<String>> = _snackbarText

    companion object{
        private const val TAG = "MainViewModel"
        private const val user = "febri009"
    }

    init {
        searchUser(user)

    }

    fun searchUser(userInput: String) {

        _isLoading.value = true

        Client.api
            .getUsers(userInput)
            .enqueue(object: Callback<UserResponse>{
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    _isLoading.value = false
                    if(response.isSuccessful && response.body() != null) {
                        _userList.postValue(response.body()?.items)
                    } else {
                        Log.e(TAG, DATA_NULL)
                        _snackbarText.value = State(DATA_NULL)
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, NO_RESPON)
                    _snackbarText.value = State(NO_RESPON)
                }

            })
    }
}