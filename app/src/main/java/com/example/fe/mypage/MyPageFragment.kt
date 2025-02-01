package com.example.fe.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.fe.R
import com.example.fe.databinding.FragmentMypageBinding
import com.example.fe.mypage.adapter.MyPageVPAdapter
import com.example.fe.setting.SettingHomeFragment
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
            val settingHomeFragment = SettingHomeFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, settingHomeFragment)
                .addToBackStack(null)
                .commit()
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

    // TabLayout 비활성화 메소드
    fun setTabLayoutEnabled(enabled: Boolean) {
        binding.mypageContentTl.isEnabled = enabled
        binding.mypageContentTl.alpha = if (enabled) 1.0f else 0.5f // 비활성화 상태에서 투명도 조정
    }
}
