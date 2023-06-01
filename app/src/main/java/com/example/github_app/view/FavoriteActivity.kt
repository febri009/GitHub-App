package com.example.github_app.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github_app.adapter.UserListAdapter
import com.example.github_app.databinding.ActivityFavoriteBinding
import com.example.github_app.viewmodel.FavoriteViewModel
import com.example.github_app.local.FavoriteUser
import com.example.github_app.model.Users

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFav.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFav.addItemDecoration(itemDecoration)

        favoriteViewModel.getFavoriteUser()?.observe(this) { items ->
            binding.rvFav.adapter = showRecyclerList(mapList(items))
        }

    }

    private fun showRecyclerList(list: List<Users?>?): UserListAdapter {
        val listUser = ArrayList<Users>()

        list?.let {
            listUser.addAll(it.filterNotNull())
        }

        val adapter = UserListAdapter(listUser)

        adapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
                rvUtils.changeActivity(
                    this@FavoriteActivity,
                    DetailUserActivity::class.java,
                    data.login
                )
            }
        })
        return adapter
    }
    object rvUtils {
        fun changeActivity(context: Context, target: Class<*>, usn: String?) {
            val moveIntent = Intent(context, target)
            usn.let { moveIntent.putExtra("data", usn) }
            context.startActivity(moveIntent)
        }
    }
    private fun mapList(users: List<FavoriteUser>): ArrayList<Users>  {
        val listUsers = ArrayList<Users>()
        for (user in users){
            val userMapped = Users(
                user.login,
                user.id,
                user.avatarUrl
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }
}