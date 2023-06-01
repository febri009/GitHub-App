package com.example.github_app.api

import com.example.github_app.model.DetailUserResponse
import com.example.github_app.model.UserResponse
import com.example.github_app.model.Users
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetails(
        @Path("username") username: String?
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getUserFollowers(
        @Path("username") username: String?
    ): Call<List<Users>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getUserFollowing(
        @Path("username") username: String?
    ): Call<List<Users>>
}