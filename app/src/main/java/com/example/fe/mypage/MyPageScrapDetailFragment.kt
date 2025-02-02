package com.example.fe.mypage

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fe.databinding.FragmentMypageScrapDetailBinding
import com.example.fe.mypage.adapter.MyPageScrapDetailRVAdapter

class MyPageScrapDetailFragment : Fragment() {
    lateinit var binding: FragmentMypageScrapDetailBinding
    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageScrapDetailBinding.inflate(inflater, container, false)

        initReviewRecyclerview()

        binding.mypageScrapEditTv.setOnClickListener {
            toggleEditMode()
        }

        binding.mypageScrapDetailBackBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }

    private fun initReviewRecyclerview() {
        binding.mypageScrapDetailRv.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        val myPageScrapDetailRVAdapter = MyPageScrapDetailRVAdapter(object : MyPageScrapDetailRVAdapter.MyItemClickListener {
            override fun onItemClick(myPageScrap: MyPageScrap) {
                // 아이템 클릭 시 처리 로직
            }

            override fun onSelectionChanged(selectedCount: Int) {
                // 선택 상태 변경 시 버튼 활성화 상태 업데이트
                binding.scrapMoveBtn.isEnabled = selectedCount > 0
                binding.scrapDeleteBtn.isEnabled = selectedCount > 0
            }
        })

        val dummyScraps = listOf(
            MyPageScrap("1"),
            MyPageScrap("2"),
            MyPageScrap("3"),
            MyPageScrap("4"),
            MyPageScrap("5"),
            MyPageScrap("6")
        )

        myPageScrapDetailRVAdapter.setScrap(dummyScraps)
        binding.mypageScrapDetailRv.adapter = myPageScrapDetailRVAdapter
    }

    private fun toggleEditMode() {
        isEditMode = !isEditMode

        // 편집 모드에 따라 버튼 보이기/숨기기
        if (isEditMode) {
            binding.bottomBtnsLl.visibility = View.VISIBLE
            binding.mypageScrapEditTv.text = "취소"
            binding.mypageScrapEditTv.setTextColor(Color.parseColor("#FF6363"))
        } else {
            binding.bottomBtnsLl.visibility = View.GONE
            binding.mypageScrapEditTv.text = "편집"
            binding.mypageScrapEditTv.setTextColor(Color.parseColor("#CFF305"))

            val adapter = binding.mypageScrapDetailRv.adapter as MyPageScrapDetailRVAdapter
            adapter.clearSelection()
        }

        binding.scrapMoveBtn.isEnabled = false
        binding.scrapDeleteBtn.isEnabled = false

        // 버튼 상태 업데이트
        binding.scrapMoveBtn.alpha = if (isEditMode) 1.0f else 0.5f
        binding.scrapDeleteBtn.alpha = if (isEditMode) 1.0f else 0.5f

        // 리사이클러뷰의 어댑터에 편집 모드 상태 전달
        (binding.mypageScrapDetailRv.adapter as MyPageScrapDetailRVAdapter).setEditMode(isEditMode)
    }
}
