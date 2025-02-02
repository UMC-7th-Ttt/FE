package com.example.fe

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fe.bookclub_book.BookclubBookFragment
import com.example.fe.bookclub_place.BookclubPlaceFragment
import com.example.fe.databinding.ActivityMainBinding
import com.example.fe.mypage.MyPageFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initBottomNavigation()

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment()) // 앱 실행 시 기본 프래그먼트 (홈 화면)
            binding.bottomNavigation.selectedItemId = R.id.bottom_nav_home
        }

    }

    private fun initBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_home -> replaceFragment(HomeFragment(), showBottomNav = true)
                R.id.bottom_nav_bookclub_book -> replaceFragment(BookclubBookFragment(), showBottomNav = true)
                R.id.bottom_nav_bookclub_place -> replaceFragment(BookclubPlaceFragment(), showBottomNav = true)
                R.id.bottom_nav_mypage -> replaceFragment(MyPageFragment(), showBottomNav = true)
            }
            true
        }
    }

    // 프래그먼트 변경 시 바텀 네비게이션 숨기기 기능 추가
    fun replaceFragment(fragment: Fragment, showBottomNav: Boolean = true) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm_in_bottom_nav, fragment)
            .addToBackStack(null)
            .commit()

        showBottomNavigation(showBottomNav)
    }

    // 바텀 네비게이션 표시/숨김 함수 추가
    fun showBottomNavigation(isVisible: Boolean) {
        binding.bottomNavigation.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}
