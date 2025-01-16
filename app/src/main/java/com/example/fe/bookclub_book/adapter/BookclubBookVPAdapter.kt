package com.example.fe.bookclub_book.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fe.bookclub_book.BookclubBookActivity
import com.example.fe.bookclub_book.BookclubBookHomeFragment
import com.example.fe.bookclub_book.BookclubBookParticipationFragment


class BookclubBookVPAdapter(activity: BookclubBookActivity) : FragmentStateAdapter(activity)  {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BookclubBookHomeFragment()
            1 -> BookclubBookParticipationFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}