package com.example.fe.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fe.databinding.FragmentMypageScrapBinding

class MyPageScrapFragment:Fragment() {
    lateinit var binding:FragmentMypageScrapBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageScrapBinding.inflate(inflater, container, false)

        initScrapRecyclerview()

        return binding.root
    }

    private fun initScrapRecyclerview(){
        binding.scrapRv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val myPageScrapRVAdapter = MyPageScrapRVAdapter()

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