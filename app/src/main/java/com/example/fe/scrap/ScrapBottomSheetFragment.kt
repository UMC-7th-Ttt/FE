package com.example.fe.scrap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R
import com.example.fe.databinding.FragmentScrapBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ScrapBottomSheetFragment(
    private val onBookmarkStateChanged: (Boolean) -> Unit // 선택/해제 상태 콜백 추가
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentScrapBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScrapBottomSheetBinding.inflate(inflater, container, false)

        initScrapBottomSheetRV()
        initListeners()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setDimAmount(0.5f) // 배경을 50% 어둡게
    }

    private fun initScrapBottomSheetRV() {
        val scrapList = listOf(
            Pair("도서", R.drawable.img_scrap_book),
            Pair("공간", R.drawable.img_scrap_place),
            Pair("뇌과학..🧠", R.drawable.img_scrap_user_add)
        )

//        val adapter = ScrapBottomSheetRVAdapter(scrapList) { isSelected ->
//            onBookmarkStateChanged(isSelected) // 선택/해제 상태 콜백 호출
//        }

        val adapter = ScrapBottomSheetRVAdapter(scrapList, { isSelected ->
            onBookmarkStateChanged(isSelected)
        }, this) // 현재 ScrapBottomSheetFragment 넘김

        binding.scrapBottomSheetRv.adapter = adapter
        binding.scrapBottomSheetRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    private fun initListeners() {
        // "새 스크랩" 클릭 시 다이얼로그 띄우기
        binding.newScrapTv.setOnClickListener {
            val dialog = NewScrapDialogFragment {
                onBookmarkStateChanged(true) // 북마크 상태 변경
            }
            dismiss() // ScrapBottomSheetFragment 닫기
            dialog.show(parentFragmentManager, "NewScrapDialogFragment")
        }
    }

}
