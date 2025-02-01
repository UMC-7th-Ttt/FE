package com.example.fe.bookclub_book

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.viewpager2.widget.ViewPager2
import com.example.fe.R
import com.example.fe.bookclub_book.adapter.BookclubBookVPAdapter
import com.example.fe.bookclub_book.dataclass.BookclubParticipation
import com.example.fe.databinding.ActivitiyBookclubBookBinding
import com.example.fe.databinding.ActivityMainBinding
import com.example.fe.mypage.MyPageFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class BookclubBookActivity: AppCompatActivity() {
    lateinit var binding: ActivitiyBookclubBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitiyBookclubBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bookclubBookPersonIc.setOnClickListener{
            val mypageFragment = MyPageFragment()
            supportFragmentManager.commit {
                replace(R.id.fragment_container, mypageFragment)
                addToBackStack(null)
            }
        }

        val tabLayout: TabLayout = findViewById(R.id.bookclub_book_content_tl)
        val viewPager: ViewPager2 = findViewById(R.id.bookclub_book_content_vp)

        // Set up the ViewPager adapter
        val adapter = BookclubBookVPAdapter(this)
        viewPager.adapter = adapter

        // Link TabLayout and ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "홈"
                1 -> "참여현황"
                else -> null
            }
        }.attach()
    }
}
