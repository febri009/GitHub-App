package com.example.github_app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github_app.model.Users
import com.example.github_app.State
import com.example.github_app.State.Companion.DATA_NULL
import com.example.github_app.State.Companion.NO_RESPON
import com.example.github_app.api.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowsViewModel : ViewModel() {

    companion object {
        const val TAG = "FollowViewModel"
    }

    private val _userFollowers = MutableLiveData<List<Users?>?>()
    val userFollowers: LiveData<List<Users?>?> = _userFollowers

    private val _userFollowing = MutableLiveData<List<Users?>?>()
    val userFollowing: LiveData<List<Users?>?> = _userFollowing

    private val _isLoadingF = MutableLiveData<Boolean>()
    val isLoadingF: LiveData<Boolean> = _isLoadingF

    private val _snackbarTextF = MutableLiveData<State<String>>()
    val snackbarTextF: LiveData<State<String>> = _snackbarTextF

    fun getUserFollowers(username: String?) {

        _isLoadingF.value = true

        Client.api
            .getUserFollowers(username)
            .enqueue(object: Callback<List<Users>>{
                override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                    _isLoadingF.value = false
                    if (response.isSuccessful && response.body() != null){
                        _userFollowers.value= response.body()
                    } else {
                        Log.e(TAG, DATA_NULL)
                        _snackbarTextF.value = State(DATA_NULL)
                    }
                }
                override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                    _isLoadingF.value = false
                    Log.e(TAG, NO_RESPON)
                    _snackbarTextF.value = State(NO_RESPON)
                }

            })
    }


    fun getUserFollowing(username: String?) {

        _isLoadingF.value = true

        Client.api
            .getUserFollowing(username)
            .enqueue(object: Callback<List<Users>>{
                override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                    _isLoadingF.value = false
                    if (response.isSuccessful && response.body() != null){
                        _userFollowing.value = response.body()
                    } else {
                        Log.e(TAG, DATA_NULL)
                        _snackbarTextF.value = State(DATA_NULL)
                    }
                }
                override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                    _isLoadingF.value = false
                    Log.e(TAG, NO_RESPON)
                    _snackbarTextF.value = State(NO_RESPON)
                }

            })
    }

}