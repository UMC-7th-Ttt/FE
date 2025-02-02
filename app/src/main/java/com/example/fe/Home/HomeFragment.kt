package com.example.fe.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.fe.Home.Category.HomeBook
import com.example.fe.Home.Category.HomeCategory
import com.example.fe.Home.Category.HomeCategoryAdapter
import com.example.fe.R

class HomeFragment : Fragment() {

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // 세로 스크롤 리사이클러뷰 설정
        val recyclerView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recyclerView.adapter = HomeCategoryAdapter(getCategoryList())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewPager 설정
        viewPager = view.findViewById(R.id.view_pager)

        val bannerList = listOf(
            BannerItem(R.drawable.viewpager_sample2, "삶에 대한 고찰", "청춘이 말하는 삶의 의미", "파묵"),
            BannerItem(R.drawable.viewpager_sample2, "행복의 철학", "작은 것에서 찾는 행복", "알랭 드 보통"),
            BannerItem(R.drawable.viewpager_sample2, "자기 계발의 힘", "성공하는 사람들의 비밀", "브라이언 트레이시")
        )

        viewPager.adapter = ViewPagerAdapter(bannerList)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    private fun getCategoryList(): List<HomeCategory> {
        return listOf(
            HomeCategory("인기 도서", listOf(HomeBook(R.drawable.book_sample1, "책 1"))),
            HomeCategory("추천 도서", listOf(HomeBook(R.drawable.book_sample1, "책 2")))
        )
    }
}