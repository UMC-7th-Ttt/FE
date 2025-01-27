package com.example.fe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.FragmentScrapBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ScrapBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentScrapBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScrapBottomSheetBinding.inflate(inflater, container, false)

        initScrapBottomSheetRV()

        return binding.root
    }

    private fun initScrapBottomSheetRV() {

//        // "새 스크랩" 클릭 이벤트 처리
//        binding.newScrapTv.setOnClickListener {
//            // "새 스크랩" 클릭 시 동작
//            dismiss() // 바텀시트 닫기
//        }

        val scrapList = listOf(
            Pair("도서", R.drawable.img_scrap_book),
            Pair("공간", R.drawable.img_scrap_place),
            Pair("뇌과학..🧠", R.drawable.img_scrap_user_add)
        )

        val adapter = ScrapBottomSheetRVAdapter(scrapList)
        binding.scrapBottomSheetRv.adapter = adapter
        binding.scrapBottomSheetRv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

    }
}
