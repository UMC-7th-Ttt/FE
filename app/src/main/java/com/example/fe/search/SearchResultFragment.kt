package com.example.fe.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.fe.databinding.FragmentSearchResultBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SearchResultFragment : Fragment() {
    private lateinit var binding: FragmentSearchResultBinding
    private lateinit var keyword: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)

        keyword = arguments?.getString("KEYWORD") ?: ""

        setupViewPager()

        return binding.root
    }

    private fun setupViewPager() {
        val viewPager: ViewPager2 = binding.searchResultVp
        val tabLayout: TabLayout = binding.searchResultTl

        val adapter = SearchResultVPAdapter(this, keyword)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "도서"
                else -> "공간"
            }
        }.attach()
    }
}
