package com.example.fe

import HomeFragment
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.example.fe.bookclub_book.BookclubBookFragment
import com.example.fe.bookclub_place.BookclubPlaceFragment
import com.example.fe.mypage.MyPageFragment
import com.example.fe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val showFragment = intent.getStringExtra("SHOW_FRAGMENT")
        if (showFragment == "MyPageFragment") {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_layout, MyPageFragment()) // ✅ MyPageFragment로 변경
                .commit()
        }//리뷰에서 메인으로오면 마이프레그먼트 띄우도록

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
                R.id.bottom_nav_home -> {
                    replaceFragment(HomeFragment(), showBottomNav = true)
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_nav_bookclub_book -> {
                    replaceFragment(BookclubBookFragment(), showBottomNav = true)
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_nav_bookclub_place -> {
                    replaceFragment(BookclubPlaceFragment(), showBottomNav = true)
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_nav_mypage -> {
                    replaceFragment(MyPageFragment(), showBottomNav = true)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    fun replaceFragment(fragment: Fragment, showBottomNav: Boolean = true) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm_in_bottom_nav, fragment)
            .commit()

        showBottomNavigation(showBottomNav)
    }

    fun addFragment(fragment: Fragment, showBottomNav: Boolean = true) {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_frm_in_bottom_nav, fragment) // replace 대신 add 사용
            .addToBackStack(null) // 백스택에 추가하여 뒤로 가기 가능하게 설정
            .commit()
        showBottomNavigation(showBottomNav)
    }

    // 바텀 네비게이션 표시/숨김 함수 추가
    fun showBottomNavigation(isVisible: Boolean) {
        binding.bottomNavigation.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}
