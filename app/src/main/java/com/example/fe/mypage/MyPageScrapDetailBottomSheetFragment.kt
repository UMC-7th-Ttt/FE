package com.example.fe.mypage

import com.example.fe.scrap.ScrapBottomSheetRVAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R
import com.example.fe.databinding.FragmentMypageScrapDetailBottomSheetBinding
import com.example.fe.databinding.FragmentScrapBottomSheetBinding
import com.example.fe.mypage.adapter.MyPageScrapDetailBottomSheetRVAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MyPageScrapDetailBottomSheetFragment(
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMypageScrapDetailBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMypageScrapDetailBottomSheetBinding.inflate(inflater, container, false)

        initScrapBottomSheetRV()

        return binding.root
    }

    private fun initScrapBottomSheetRV() {
        val scrapList = listOf(
            Pair("도서", R.drawable.img_scrap_book),
            Pair("공간", R.drawable.img_scrap_place),
            Pair("뇌과학..🧠", R.drawable.img_scrap_user_add)
        )

        val adapter = MyPageScrapDetailBottomSheetRVAdapter(scrapList)

        binding.scrapBottomSheetRv.adapter = adapter
        binding.scrapBottomSheetRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

}
