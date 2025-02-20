package com.example.fe.bookclub_book

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.viewpager2.widget.ViewPager2
import com.example.fe.MainActivity
import com.example.fe.mypage.MyPageFragment
import com.example.fe.R
import com.example.fe.bookclub_book.adapter.BookclubBookVPAdapter
import com.example.fe.bookclub_book.server.BookClubUserResponse
import com.example.fe.JohnRetrofitClient
import com.example.fe.bookclub_book.server.BookClubRetrofitInterface
import com.example.fe.databinding.FragmentBookclubBookBinding
import com.example.fe.search.SearchMainActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.bumptech.glide.Glide

class BookclubBookFragment : Fragment() {

    private lateinit var binding: FragmentBookclubBookBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookclubBookBinding.inflate(inflater, container, false)

        fetchUser()

        // 프로필 아이콘 클릭 리스너 설정
        binding.bookclubBookPersonIc.setOnClickListener {
            val mypageFragment = MyPageFragment()
            parentFragmentManager.commit {
                replace(R.id.fragment_container, mypageFragment)
                addToBackStack(null)
            }

            (activity as? MainActivity)?.binding?.bottomNavigation?.selectedItemId = R.id.bottom_nav_mypage
        }

        // 검색 아이콘 클릭 리스너 설정
        binding.bookclubBookSearchIc.setOnClickListener {
            val intent = Intent(context, SearchMainActivity::class.java)
            startActivity(intent)
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

    private fun fetchUser() {
        val api = JohnRetrofitClient.getClient(requireContext()).create(BookClubRetrofitInterface::class.java)
        api.getUser().enqueue(object : Callback<BookClubUserResponse> {
            override fun onResponse(call: Call<BookClubUserResponse>, response: Response<BookClubUserResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    userResponse?.let {
                        val profileUrl = it.result.profileUrl
                        Log.d("BookclubBookFragment", "Profile URL: $profileUrl") // 로그로 URL 확인
                        Glide.with(this@BookclubBookFragment)
                            .load(profileUrl)
                            .into(binding.bookclubBookPersonIc)
                    }
                } else {
                    Log.e("BookclubBookFragment", "Response not successful: ${response.code()} - ${response.message()}") // 실패 로그 추가
                }
            }

            override fun onFailure(call: Call<BookClubUserResponse>, t: Throwable) {
                Log.e("BookclubBookFragment", "API call failed", t) // 실패 로그 추가
            }
        })
    }
}