package com.example.fe.mypage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPageVPAdapter(activity: MyPageFragment) : FragmentStateAdapter(activity)  {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyPageReviewFragment()
            1 -> MyPageScrapFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}