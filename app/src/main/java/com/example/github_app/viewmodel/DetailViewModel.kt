package com.example.github_app.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.github_app.model.DetailUserResponse
import com.example.github_app.State
import com.example.github_app.api.Client
import com.example.github_app.local.FavoriteUser
import com.example.github_app.local.FavoriteUserDao
import com.example.github_app.local.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        private const val TAG = "DetailViewModel"
    }

    private val _Detail = MutableLiveData<DetailUserResponse>()
    val userDetail: LiveData<DetailUserResponse> = _Detail

    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase?

    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    private val _LoadingA = MutableLiveData<Boolean>()
    val loadingA: LiveData<Boolean> = _LoadingA

    private val _snackbarA = MutableLiveData<State<String>>()
    val snackbarA: LiveData<State<String>> = _snackbarA


    fun getUser(username: String?) {

        _LoadingA.value = true

        Client.api
            .getUserDetails(username)
            .enqueue(object : Callback<DetailUserResponse>{
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    _LoadingA.value = false
                    if (response.isSuccessful && response.body() != null){
                        _Detail.postValue(response.body())
                    } else{
                        Log.e(TAG, State.DATA_NULL)
                        _snackbarA.value = State(State.DATA_NULL)
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    _LoadingA.value = false
                    Log.e(TAG, State.NO_RESPON)
                    _snackbarA.value = State(State.NO_RESPON)
                }
            })
    }

    fun addToFavorite(login: String, id: Int, avatarUrl:String){
        CoroutineScope(Dispatchers.IO).launch{
            val user = FavoriteUser(
                login,
                id,
                avatarUrl
            )
            userDao?.addToFavorite(user)
        }
    }
    fun checkUser(login: String) = userDao?.checkUser(login)
    fun removeFromFavorite(login: String){
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(login)
        }
    }
}