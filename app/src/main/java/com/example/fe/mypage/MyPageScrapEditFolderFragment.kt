package com.example.fe.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.fe.R
import com.example.fe.databinding.FragmentMypageScrapEditFolderBinding
import com.example.fe.mypage.adapter.MyPageScrapRVAdapter

class MyPageScrapEditFolderFragment:Fragment() {
    lateinit var binding: FragmentMypageScrapEditFolderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageScrapEditFolderBinding.inflate(inflater, container, false)

        binding.mypageScrapEditCancelTv.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        initScrapRecyclerview()


        return binding.root
    }

    private fun initScrapRecyclerview(){
        binding.scrapRv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val myPageScrapRVAdapter = MyPageScrapRVAdapter(object : MyPageScrapRVAdapter.MyItemClickListener {
            override fun onItemClick(myPageScrap: MyPageScrap) {

            }
        })

        val dummyScrap = listOf(
            MyPageScrap("1"),
            MyPageScrap("2"),
            MyPageScrap("3"),
            MyPageScrap("4")
        )

        myPageScrapRVAdapter.setScrap(dummyScrap)

        binding.scrapRv.adapter = myPageScrapRVAdapter

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