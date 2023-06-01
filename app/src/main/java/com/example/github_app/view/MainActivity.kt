package com.example.github_app.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github_app.R
import com.example.github_app.adapter.UserListAdapter
import com.example.github_app.databinding.ActivityMainBinding
import com.example.github_app.model.Users
import com.example.github_app.preference.Setting
import com.example.github_app.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        mainViewModel.userList.observe(this) { items ->
            binding.rvUser.adapter = showRecyclerList(items)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        val pref = Setting.getInstance(dataStore)
        val settingViewModelFactory = SettingViewModel(pref).createFactory()
        val settingViewModel = ViewModelProvider(this, settingViewModelFactory)[SettingViewModel::class.java]
        settingViewModel.themeSetting().observe(this) {isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.tulisan_search)

        mainViewModel.searchQuery.observe(this) { query ->
            searchView.setQuery(query.toString(), false)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    mainViewModel.searchUser(query)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                mainViewModel.searchQuery.postValue(query)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting) {
            Intent(this@MainActivity, SettingActivity::class.java).apply{
                startActivity(this)
            }
        }
        if (item.itemId == R.id.favorite){
            Intent(this@MainActivity, FavoriteActivity::class.java).apply {
                startActivity(this)
            }
        }
        return super.onOptionsItemSelected(item)
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
                    this@MainActivity,
                    DetailUserActivity::class.java,
                    data.login
                )
            }
        })
        return adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    object rvUtils {
        fun changeActivity(context: Context, target: Class<*>, usn: String?) {
            val moveIntent = Intent(context, target)
            usn.let { moveIntent.putExtra("data", usn) }
            context.startActivity(moveIntent)
        }
    }
}

