package com.example.fe.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.fe.R
import com.example.fe.databinding.FragmentMypageBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MyPageFragment:Fragment() {

    lateinit var binding: FragmentMypageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)

        val tabLayout: TabLayout = binding.mypageContentTl
        val viewPager: ViewPager2 = binding.mypageContentVp

        // Set up the ViewPager adapter
        val adapter = MyPageVPAdapter(this)
        viewPager.adapter = adapter

        // Link TabLayout and ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "서평"
                1 -> "스크랩"
                else -> null
            }
        }.attach()

        binding.mypageSettingIv.setOnClickListener{
            val mypageSettingFragment = MyPageSettingFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mypageSettingFragment)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

}