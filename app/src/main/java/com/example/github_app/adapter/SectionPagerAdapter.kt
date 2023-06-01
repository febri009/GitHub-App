package com.example.github_app.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.github_app.view.FollowernFollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var usn: String? = "test"

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowernFollowingFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowernFollowingFragment.ARG_POSISI, position + 1)
            putString(FollowernFollowingFragment.ARG_USN, usn)
        }
        return fragment
    }

    override fun getItemCount() = 2
}