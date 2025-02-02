package com.example.fe.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.fe.setting.SettingActivity
import com.example.fe.databinding.FragmentMypageBinding
import com.example.fe.mypage.adapter.MyPageVPAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MyPageFragment : Fragment() {

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

        // Settings button click listener
        binding.mypageSettingIv.setOnClickListener {
            context?.let { ctx ->
                val intent = Intent(ctx, SettingActivity::class.java)
                startActivity(intent)
            }
        }

        // TabLayout 클릭 리스너
        setupTabLayout(tabLayout, viewPager)

        return binding.root
    }

    private fun setupTabLayout(tabLayout: TabLayout, viewPager: ViewPager2) {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // TabLayout이 활성화 상태일 때만 ViewPager2의 Fragment로 전환됨
                if (tabLayout.isEnabled) { // TabLayout이 활성화 상태인지 확인
                    viewPager.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}
