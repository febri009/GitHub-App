package com.example.github_app.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_user")
data class FavoriteUser(
    @PrimaryKey
    val login: String,
    val id: Int,
    val avatarUrl : String
)