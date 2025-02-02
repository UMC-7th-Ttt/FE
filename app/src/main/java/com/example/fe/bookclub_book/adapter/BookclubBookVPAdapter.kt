package com.example.fe.bookclub_book.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fe.R
import com.example.fe.bookclub_book.BookclubBookFragment
import com.example.fe.bookclub_book.BookclubBookHomeFragment
import com.example.fe.bookclub_book.BookclubBookParticipationFragment


class BookclubBookVPAdapter(activity: BookclubBookFragment) : FragmentStateAdapter(activity)  {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BookclubBookHomeFragment()
            1 -> BookclubBookParticipationFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }

    fun showBookclubBookHomeFragment(activity: AppCompatActivity) {
        val bookclubBookHomeFragment = BookclubBookHomeFragment() // 전체 화면 프래그먼트
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.bookclub_book_content_vp, bookclubBookHomeFragment)
            .addToBackStack(null)
            .commit()
    }
}