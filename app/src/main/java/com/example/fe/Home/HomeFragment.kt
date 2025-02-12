package com.example.fe.Home

import HorizontalItemDecoration
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide

import com.example.app.ui.home.HomeBookAdapter
import com.example.fe.Home.Category.HomeBook
import com.example.fe.Notification.NotificationActivity

import com.example.fe.databinding.FragmentHomeBinding
import com.example.fe.network.RetrofitObj
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    //    private val binding get() = _binding!!
    private val homeService = RetrofitObj.getRetrofit().create(HomeApiService::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("HomeFragment", "🟢 HomeFragment 실행됨")
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🔹 API 데이터 불러오기
        setupHomeData()

        // 🔹 알림 버튼 클릭 이벤트
        binding.notificationIcon.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }

    private fun setupHomeData() {
        val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTczOTc2ODE2MiwiZW1haWwiOiJtb2Rlc3RuYXR1cmVAbmF2ZXIuY29tIn0.rGfiXRBkJ1x1mpFl5I1LDBClf_TddLfj0e0l_YfgtWU-DqEQ83yXNdicdBmz9k8tmAyqK5iNHAafTdvKo8RIsg" //  토큰 추가
        homeService.getHomeData("Bearer $token").enqueue(object : Callback<HomeResponse> {
            override fun onResponse(call: Call<HomeResponse>, response: Response<HomeResponse>) {
                if (response.isSuccessful) {
                    val homeData = response.body()?.result
                    if (homeData != null) {
                        Log.d("API", "✅ 데이터 불러오기 성공")
                        updateUI(homeData)
                    }
                } else {
                    Log.e("API", "❌ 응답 실패: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                Log.e("API", "❌ API 호출 실패", t)
                Toast.makeText(requireContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(data: HomeResult) {
        // 🔹 프로필 설정
        binding.greetingText.text = "안녕하세요, ${data.nickname}님!\n오늘은 어떤 책을 시작해볼까요?"
        Glide.with(this).load(data.profileUrl).into(binding.profileIcon)

        // 🔹 배너 ViewPager 설정
        binding.viewPager.adapter = ViewPagerAdapter(data.mainBannerList)
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 🔹 리사이클러뷰 (카테고리별 책 리스트)
        val recyclerViews = listOf(
            binding.verticalRecyclerView1, binding.verticalRecyclerView2,
            binding.verticalRecyclerView3, binding.verticalRecyclerView4, binding.verticalRecyclerView5
        )

        val categoryBookLists = data.bookLetterList.map { it.bookList.map { book -> HomeBook(book.bookCoverImg) } }

        for (i in recyclerViews.indices) {
            recyclerViews[i].layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            recyclerViews[i].adapter = HomeBookAdapter(categoryBookLists.getOrNull(i) ?: emptyList())
        }
    }
}
