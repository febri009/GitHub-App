package com.example.github_app.model

import com.google.gson.annotations.SerializedName

data class Users(

	@SerializedName("login")
	val login: String?,
	@SerializedName("id")
	val id: Int?,
	@SerializedName("avatar_url")
	val avatarUrl: String?,
)
