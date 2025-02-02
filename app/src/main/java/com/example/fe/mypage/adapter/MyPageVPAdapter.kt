package com.example.fe.mypage.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fe.mypage.MyPageFragment
import com.example.fe.mypage.MyPageReviewCalendarFragment
import com.example.fe.mypage.MyPageScrapFragment

class MyPageVPAdapter(activity: MyPageFragment) : FragmentStateAdapter(activity)  {

    override fun getItemCount(): Int = 2

    @RequiresApi(Build.VERSION_CODES.O)
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyPageReviewCalendarFragment()
            1 -> MyPageScrapFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}