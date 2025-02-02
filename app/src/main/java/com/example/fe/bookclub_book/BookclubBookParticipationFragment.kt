package com.example.fe.bookclub_book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R
import com.example.fe.bookclub_book.dataclass.BookclubParticipation
import com.example.fe.bookclub_book.adapter.BookclubParticipationRVAdapter
import com.example.fe.databinding.FragmentBookclubBookParticipationBinding

class BookclubBookParticipationFragment: Fragment() {
    lateinit var binding: FragmentBookclubBookParticipationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookclubBookParticipationBinding.inflate(inflater, container, false)

        initParticipationRecyclerview()

        return binding.root
    }

    private fun initParticipationRecyclerview() {
        binding.bookclubParticipationRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.bookclubParticipationRv)

        // 어댑터에 클릭 리스너 추가
        val bookclubParticipationRVAdapter = BookclubParticipationRVAdapter(object : BookclubParticipationRVAdapter.MyItemClickListener {
            override fun onItemClick(participation: BookclubParticipation) {
                // Fragment 전환
                val detailFragment = BookclubBookDetailFragment() // 데이터 전달 없이 단순 전환
                parentFragmentManager.commit {
                    replace(R.id.fragment_container, detailFragment)
                    addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 설정
                }
            }
        })

        val dummyParticipation = listOf(
            BookclubParticipation("1", 43),
            BookclubParticipation("2", 25),
            BookclubParticipation("3", 84),
            BookclubParticipation("4", 100)
        )

        bookclubParticipationRVAdapter.setParticipation(dummyParticipation)
        binding.bookclubParticipationRv.adapter = bookclubParticipationRVAdapter

        binding.bookclubParticipationRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstVisibleItemPosition = (binding.bookclubParticipationRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                val lastVisibleItemPosition = (binding.bookclubParticipationRv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                // 첫 번째 아이템이 보일 때 prev 버튼 숨기기
                binding.itemParticipationPrevBtn.visibility = if (firstVisibleItemPosition == 0) View.INVISIBLE else View.VISIBLE

                // 마지막 아이템이 보일 때 next 버튼 숨기기
                binding.itemParticipationNextBtn.visibility = if (lastVisibleItemPosition == bookclubParticipationRVAdapter.itemCount - 1) View.INVISIBLE else View.VISIBLE
            }
        })

        binding.itemParticipationPrevBtn.setOnClickListener {
            val currentPos = (binding.bookclubParticipationRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            val prevPosition = currentPos - 1
            if (prevPosition >= 0) {
                binding.bookclubParticipationRv.smoothScrollToPosition(prevPosition)
            }
        }

        binding.itemParticipationNextBtn.setOnClickListener {
            val currentPos = (binding.bookclubParticipationRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            val nextPosition = currentPos + 1
            if (nextPosition < bookclubParticipationRVAdapter.itemCount) {
                binding.bookclubParticipationRv.smoothScrollToPosition(nextPosition)
            }
        }
    }
}
