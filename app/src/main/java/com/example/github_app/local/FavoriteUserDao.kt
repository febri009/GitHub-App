package com.example.github_app.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteUserDao {
    @Insert
    fun addToFavorite(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM favorite_user")
    fun getFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT count(*) FROM favorite_user WHERE favorite_user.login = :login")
    fun checkUser(login: String): Int

    @Query("DELETE FROM favorite_user WHERE favorite_user.login = :login")
    fun removeFromFavorite(login: String): Int
}