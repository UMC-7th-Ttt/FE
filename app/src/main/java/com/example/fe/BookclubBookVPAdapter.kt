package com.example.fe

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class BookclubBookVPAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity)  {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BookclubBookHomeFragment()
            1 -> BookclubBookParticipationFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}