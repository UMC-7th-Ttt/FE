package com.example.fe.mypage

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fe.R
import com.example.fe.bookclub_book.BookclubJoin
import com.example.fe.bookclub_book.dataclass.BookclubByMonth
import com.example.fe.databinding.FragmentMypageScrapBinding
import com.example.fe.mypage.adapter.MyPageScrapDetailRVAdapter
import com.example.fe.mypage.adapter.MyPageScrapRVAdapter

class MyPageScrapFragment : Fragment() {
    lateinit var binding: FragmentMypageScrapBinding
    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageScrapBinding.inflate(inflater, container, false)

        initScrapRecyclerview()

        binding.mypageScrapEditTv.setOnClickListener {
            toggleEditMode()
        }

        return binding.root
    }

    private fun toggleEditMode() {
        isEditMode = !isEditMode

        // 편집 모드에 따라 버튼 보이기/숨기기
        if (isEditMode) {
            binding.folderDeleteBtn.visibility = View.VISIBLE
            binding.mypageScrapEditTv.text = "취소"
            binding.mypageScrapEditTv.setTextColor(Color.parseColor("#FF6363"))
        } else {
            binding.folderDeleteBtn.visibility = View.GONE
            binding.mypageScrapEditTv.text = "편집"
            binding.mypageScrapEditTv.setTextColor(Color.parseColor("#CFF305"))

            val adapter = binding.scrapRv.adapter as MyPageScrapRVAdapter
            adapter.clearSelection()
        }

        // 버튼 상태 업데이트
        binding.folderDeleteBtn.isEnabled = false
        binding.folderDeleteBtn.alpha = if (isEditMode) 1.0f else 0.5f

        // 리사이클러뷰의 어댑터에 편집 모드 상태 전달
        (binding.scrapRv.adapter as MyPageScrapRVAdapter).setEditMode(isEditMode)
    }

    private fun initScrapRecyclerview() {
        binding.scrapRv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val myPageScrapRVAdapter = MyPageScrapRVAdapter(object : MyPageScrapRVAdapter.MyItemClickListener {
            override fun onItemClick(myPageScrapFolder: MyPageScrapFolder) {
                if(!isEditMode){
                    val intent = Intent(context, MyPageScrapDetail::class.java)
                    startActivity(intent)
                }
            }

            override fun onSelectionChanged(selectedCount: Int) {
                // 선택 상태 변경 시 버튼 활성화 상태 업데이트
                binding.folderDeleteBtn.isEnabled = selectedCount > 0
            }
        })

        val dummyScrapFolder = listOf(
            MyPageScrapFolder("1"),
            MyPageScrapFolder("2"),
            MyPageScrapFolder("3"),
            MyPageScrapFolder("4")
        )

        myPageScrapRVAdapter.setScrap(dummyScrapFolder)
        binding.scrapRv.adapter = myPageScrapRVAdapter
    }
}
