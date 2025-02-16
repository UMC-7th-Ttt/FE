package com.example.fe.bookclub_book

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.bookclub_book.adapter.BookclubParticipationRVAdapter
import com.example.fe.bookclub_book.dataclass.BookClubParticipationResponse
import com.example.fe.bookclub_book.server.api
import com.example.fe.databinding.FragmentBookclubBookParticipationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookclubBookParticipationFragment: Fragment() {
    lateinit var binding: FragmentBookclubBookParticipationBinding
    private lateinit var bookclubParticipationRVAdapter: BookclubParticipationRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookclubBookParticipationBinding.inflate(inflater, container, false)

        initParticipationRecyclerview()
        fetchParticipations()

        return binding.root
    }

    // 북클럽 멤버 리사이클러뷰 초기화 함수
    private fun initParticipationRecyclerview() {
        binding.bookclubParticipationRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.bookclubParticipationRv)

        bookclubParticipationRVAdapter = BookclubParticipationRVAdapter(object : BookclubParticipationRVAdapter.MyItemClickListener {
            override fun onItemClick(participation: BookClubParticipationResponse.Result.BookClub) {
                val intent = Intent(context, BookclubBookDetail::class.java).apply {
                    putExtra("bookClubId", participation.bookClubId)
//                    putExtra("bookId", participation.bookId)
                }
                startActivity(intent)
            }
        })

        binding.bookclubParticipationRv.adapter = bookclubParticipationRVAdapter
    }

    // 참여중인 북클럽 정보 조회 함수
    private fun fetchParticipations() {
        api.getParticipation().enqueue(object : Callback<BookClubParticipationResponse> {
            override fun onResponse(call: Call<BookClubParticipationResponse>, response: Response<BookClubParticipationResponse>) {
                if (response.isSuccessful) {
                    val participationResponse = response.body()
                    participationResponse?.result?.let {
                        // n월 북클럽 텍스트 업데이트
                        binding.bookclubParticipationSubTv.text = "${it.currentMonth}월 북클럽"

                        // 북클럽 참여 정보 설정
                        bookclubParticipationRVAdapter.setParticipation(it.bookClubs)
                    }
                } else {
                    // 오류 처리
                }
            }

            override fun onFailure(call: Call<BookClubParticipationResponse>, t: Throwable) {
                // 오류 처리
            }
        })

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

        // 참여 현황 이전 북클럽 버튼
        binding.itemParticipationPrevBtn.setOnClickListener {
            val currentPos = (binding.bookclubParticipationRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            val prevPosition = currentPos - 1
            if (prevPosition >= 0) {
                binding.bookclubParticipationRv.smoothScrollToPosition(prevPosition)
            }
        }

        // 참여 현황 다음 북클럽 버튼
        binding.itemParticipationNextBtn.setOnClickListener {
            val currentPos = (binding.bookclubParticipationRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            val nextPosition = currentPos + 1
            if (nextPosition < bookclubParticipationRVAdapter.itemCount) {
                binding.bookclubParticipationRv.smoothScrollToPosition(nextPosition)
            }
        }
    }
}