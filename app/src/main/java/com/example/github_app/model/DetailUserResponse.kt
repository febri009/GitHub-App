package com.example.github_app.model

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(
	@SerializedName("login")
	val login: String?,
	@SerializedName("id")
	val id: Int?,
	@SerializedName("avatar_url")
	val avatarUrl: String?,
	@SerializedName("followers_url")
	val followersUrl: String?,
	@SerializedName("following_url")
	val followingUrl: String?,
	@SerializedName("name")
	val name: String?,
	@SerializedName("location")
	val location: String?,
	@SerializedName("bio")
	val bio: String?,
	@SerializedName("followers")
	val followers: Int?,
	@SerializedName("following")
	val following: Int?,
)
