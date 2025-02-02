package com.example.fe.bookclub_book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.viewpager2.widget.ViewPager2
import com.example.fe.mypage.MyPageFragment
import com.example.fe.R
import com.example.fe.bookclub_book.adapter.BookclubBookVPAdapter
import com.example.fe.databinding.FragmentBookclubBookBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class BookclubBookFragment : Fragment() {

    private lateinit var binding: FragmentBookclubBookBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookclubBookBinding.inflate(inflater, container, false)

        // 클릭 리스너 설정
        binding.bookclubBookPersonIc.setOnClickListener {
            val mypageFragment = MyPageFragment()
            parentFragmentManager.commit {
                replace(R.id.fragment_container, mypageFragment)
                addToBackStack(null)
            }
        }

        // TabLayout 및 ViewPager 설정
        val tabLayout: TabLayout = binding.bookclubBookContentTl
        val viewPager: ViewPager2 = binding.bookclubBookContentVp

        // ViewPager 어댑터 설정
        val adapter = BookclubBookVPAdapter(this)
        viewPager.adapter = adapter

        // TabLayout과 ViewPager 연결
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "홈"
                1 -> "참여현황"
                else -> null
            }
        }.attach()

        return binding.root
    }
}
