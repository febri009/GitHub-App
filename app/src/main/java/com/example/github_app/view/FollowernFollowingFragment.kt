package com.example.github_app.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github_app.adapter.UserListAdapter
import com.example.github_app.databinding.FragmentFollowernFollowingBinding
import com.example.github_app.model.Users
import com.example.github_app.viewmodel.FollowsViewModel
import com.google.android.material.snackbar.Snackbar

class FollowernFollowingFragment : Fragment() {

    private var _binding: FragmentFollowernFollowingBinding? = null
    private val binding get() = _binding!!
    private val followViewModel by viewModels<FollowsViewModel>()

    private var posisi: Int = 0
    private var uname: String? = "pepuyy123"

    companion object {
        const val ARG_POSISI = "section_number"
        const val ARG_USN = "febri009"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFollowernFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.let {
            posisi = it.getInt(ARG_POSISI)
            uname = it.getString(ARG_USN)
        }


        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollowerFollowing.layoutManager = layoutManager
        val separatorDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollowerFollowing.addItemDecoration(separatorDecoration)

        followViewModel.userFollowers.observe(viewLifecycleOwner) { items ->
            binding.rvFollowerFollowing.adapter = showRecyclerList(items)
        }

        followViewModel.userFollowing.observe(viewLifecycleOwner) { items ->
            binding.rvFollowerFollowing.adapter = showRecyclerList(items)
        }


        followViewModel.isLoadingF.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        followViewModel.snackbarTextF.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    requireView(),
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        if (posisi == 1) {
            followViewModel.getUserFollowers(uname)
        } else {
            followViewModel.getUserFollowing(uname)
        }
    }

    private fun showRecyclerList(list: List<Users?>?): UserListAdapter {

        val userList = ArrayList<Users>()

        list?.let {
            userList.addAll(it.filterNotNull())
        }

        val adapter = UserListAdapter(userList)

        adapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
                rvUtils.changeActivity(
                    requireActivity(),
                    DetailUserActivity::class.java,
                    data.login
                )
            }
        })
        return adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) {
            binding.progressBarF.visibility = View.VISIBLE
        } else {
            binding.progressBarF.visibility = View.GONE
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