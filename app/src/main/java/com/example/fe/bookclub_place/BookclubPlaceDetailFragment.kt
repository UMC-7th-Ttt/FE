package com.example.fe.bookclub_place

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.fe.MainActivity
import com.example.fe.R
import com.example.fe.scrap.ScrapBottomSheetFragment
import com.example.fe.databinding.FragmentBookclubPlaceDetailBinding

class BookclubPlaceDetailFragment : DialogFragment() {

    private lateinit var binding: FragmentBookclubPlaceDetailBinding
    private var isBookmarked = false // 북마크 상태 추적

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookclubPlaceDetailBinding.inflate(inflater, container, false)

        // Fullscreen 설정
        requireActivity().window.apply {
            statusBarColor = Color.TRANSPARENT // 상태바를 투명하게
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 상태바 위로 레이아웃 확장
                    )
        }

        initBackBtnClickListener()
        initBookmarkClickListener()
        return binding.root
    }

    private fun initBackBtnClickListener() {
        binding.bookclubPlaceBackIv.setOnClickListener {
            parentFragmentManager.popBackStack() // 기본 뒤로 가기
        }
    }

    // 북마크 아이콘 클릭 리스너
    private fun initBookmarkClickListener() {
        binding.bookclubPlaceDetailBookmarkIv.setOnClickListener {
            val scrapBottomSheet = ScrapBottomSheetFragment { isSelected ->
                updateBookmarkState(isSelected) // 상태 업데이트
            }
            scrapBottomSheet.show(parentFragmentManager, scrapBottomSheet.tag)
        }
    }

    // 북마크 상태 업데이트 함수
    private fun updateBookmarkState(isSelected: Boolean) {
        isBookmarked = isSelected // 북마크 상태 변경
        binding.bookclubPlaceDetailBookmarkIv.setImageResource(
            if (isBookmarked) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
        )
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // 상태바 복원
        requireActivity().window.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.black)
        }

        // 바텀 네비게이션 다시 표시
        (requireActivity() as MainActivity).showBottomNavigation(true)
    }
}
