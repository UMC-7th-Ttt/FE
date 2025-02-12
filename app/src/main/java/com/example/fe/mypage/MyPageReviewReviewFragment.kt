package com.example.fe.mypage

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.fe.R
import com.example.fe.databinding.FragmentMypageReviewReviewBinding
import com.example.fe.mypage.adapter.MyPageReviewReviewRVAdapter

class MyPageReviewReviewFragment:Fragment() {
    lateinit var binding: FragmentMypageReviewReviewBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageReviewReviewBinding.inflate(inflater, container, false)

        initReviewRecyclerview()

        binding.mypageReviewCalendarSwapBtn.setOnClickListener {
            // 이전 Fragment로 돌아가기
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }

    private fun initReviewRecyclerview(){
        binding.mypageReviewRv.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        val myPageReviewReviewRVAdapter = MyPageReviewReviewRVAdapter(object : MyPageReviewReviewRVAdapter.MyItemClickListener {

            override fun onItemClick(myPageReview: MyPageReview) {
//                val writeReviewFragment = MyPageWriteReviewFragment()
//                parentFragmentManager.commit {
//                    replace(R.id.fragment_container, writeReviewFragment)
//                    addToBackStack(null)
//                }
            }
        })

        val dummyReview = listOf(
            MyPageReview("1"),
            MyPageReview("2"),
            MyPageReview("3"),
            MyPageReview("4"),
            MyPageReview("1"),
            MyPageReview("2"),
            MyPageReview("3")
        )

        myPageReviewReviewRVAdapter.setReview(dummyReview)

        binding.mypageReviewRv.adapter = myPageReviewReviewRVAdapter

    }

    override fun onResume() {
        super.onResume()
        // ViewPager2의 스와이프를 감지하여 이전 Fragment로 전환
        val viewPager: ViewPager2? = requireActivity().findViewById(R.id.mypage_content_vp)
        viewPager?.isUserInputEnabled = false // 스와이프 비활성화
    }

    override fun onPause() {
        super.onPause()
        // ViewPager2의 사용 가능 상태 복원
        val viewPager: ViewPager2? = requireActivity().findViewById(R.id.mypage_content_vp)
        viewPager?.isUserInputEnabled = true // 스와이프 활성화
    }
}