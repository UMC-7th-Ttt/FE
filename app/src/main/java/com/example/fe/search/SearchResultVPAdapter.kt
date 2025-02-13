package com.example.fe.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SearchResultVPAdapter(fragment: Fragment, private val keyword: String) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SearchResultBookFragment().apply {
                arguments = Bundle().apply {
                    putString("KEYWORD", keyword)
                }
            }
            else -> SearchResultPlaceFragment().apply {
                arguments = Bundle().apply {
                    putString("KEYWORD", keyword)
                }
            }
        }
    }
}
