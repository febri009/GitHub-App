package com.example.github_app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.github_app.R
import com.example.github_app.adapter.SectionsPagerAdapter
import com.example.github_app.databinding.ActivityDetailUserBinding
import com.example.github_app.model.DetailUserResponse
import com.example.github_app.viewmodel.DetailViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailViewModel>()
    private val sectionsPagerAdapter = SectionsPagerAdapter(this)

    companion object{
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
        private val TAB_TITLE = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val login = intent.getStringExtra("data")
        detailViewModel.getUser(login)
        sectionsPagerAdapter.usn = login

        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)


        detailViewModel.userDetail.observe(this) { item ->
            setUserData(item)
        }

        detailViewModel.loadingA.observe(this) {
            showLoading(it)
        }

        detailViewModel.snackbarA.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLE[position])
        }.attach()

        supportActionBar?.elevation = 0f

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch{
            val count = detailViewModel.checkUser(login.toString())
            withContext(Dispatchers.Main){
                if (count !=null){
                    if (count>0){
                        binding.toggleFavorite.isChecked = true
                        _isChecked = true
                    } else {
                        binding.toggleFavorite.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        binding.toggleFavorite.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked){
                detailViewModel.addToFavorite(login.toString(), id, avatarUrl.toString())
            } else {
                detailViewModel.removeFromFavorite(login.toString())
            }
            binding.toggleFavorite.isChecked = _isChecked
        }
    }

    private fun setUserData(user: DetailUserResponse) {
        binding.apply{
            tvNamaFull.text = user.name
            tvUsn.text = user.login
            Glide.with(applicationContext)
                .load(user.avatarUrl)
                .error(R.drawable.ic_person)
                .into(ivPhotoProfil)
            tvJumlahFollowers.text = user.followers.toString()
            tvJumlahFollowing.text = user.following.toString()
            if(user.bio != null) tvBio.text = user.bio
            else tvBio.visibility = View.GONE
            if(user.location != null) tvLoc.text = user.location else {
                tvLoc.visibility = View.GONE
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) {
            binding.progressBarD.visibility = View.VISIBLE
        } else {
            binding.progressBarD.visibility = View.GONE
        }
    }
}

