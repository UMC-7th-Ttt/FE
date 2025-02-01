package com.example.fe.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fe.R
import com.example.fe.databinding.FragmentMypageScrapBinding
import com.example.fe.mypage.adapter.MyPageScrapRVAdapter

class MyPageScrapFragment:Fragment() {
    lateinit var binding:FragmentMypageScrapBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageScrapBinding.inflate(inflater, container, false)

        initScrapRecyclerview()

        binding.mypageScrapEditTv.setOnClickListener {
            replaceFragment(MyPageScrapEditFolderFragment())
        }

        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.vp_fragment_change_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun initScrapRecyclerview(){
        binding.scrapRv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val myPageScrapRVAdapter = MyPageScrapRVAdapter(object : MyPageScrapRVAdapter.MyItemClickListener {
            override fun onItemClick(myPageScrap: MyPageScrap) {
                val scrapDetailFragment = MyPageScrapDetailFragment()
                // Fragment 전환 시 백 스택에 추가
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, scrapDetailFragment)
                    .addToBackStack(null) // 백 스택에 추가
                    .commit()
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

}