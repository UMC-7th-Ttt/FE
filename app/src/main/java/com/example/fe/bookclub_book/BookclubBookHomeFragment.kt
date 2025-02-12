package com.example.fe.bookclub_book

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.bookclub_book.adapter.BookclubByMonthRVAdapter
import com.example.fe.bookclub_book.adapter.BookclubMemberRVAdapter
import com.example.fe.bookclub_book.server.BookClubByMonthResponse
import com.example.fe.bookclub_book.server.ReadingRecordsListResponse
import com.example.fe.bookclub_book.server.api
import com.example.fe.databinding.FragmentBookclubBookHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookclubBookHomeFragment : Fragment() {

    private lateinit var binding: FragmentBookclubBookHomeBinding
    private lateinit var bookclubMemberRVAdapter: BookclubMemberRVAdapter
    private lateinit var bookclubByMonthRVAdapter: BookclubByMonthRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookclubBookHomeBinding.inflate(inflater, container, false)

        initBookclubMemberRecyclerview()
        initBookclubByMonthRecyclerview()
        fetchReadingRecords()
        fetchBookClubMonth()

        return binding.root
    }

    // 북클럽 멤버 리사이클러뷰 초기화 함수
    private fun initBookclubMemberRecyclerview() {
        binding.bookclubBookHomeMembersRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        bookclubMemberRVAdapter = BookclubMemberRVAdapter(object : BookclubMemberRVAdapter.MyItemClickListener {
            override fun onItemClick(readingRecordId: Int) {
                val intent = Intent(context, BookclubBookReviewDetail::class.java)
                intent.putExtra("readingRecordId", readingRecordId)
                startActivity(intent)
            }
        })

        binding.bookclubBookHomeMembersRv.adapter = bookclubMemberRVAdapter
    }

    // 월별 북클럽 리사이클러뷰 초기화 함수
    private fun initBookclubByMonthRecyclerview() {
        binding.bookclubBookHomeMonthRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        bookclubByMonthRVAdapter = BookclubByMonthRVAdapter(object : BookclubByMonthRVAdapter.MyItemClickListener {
            override fun onItemClick(bookClub: BookClubByMonthResponse.Result.BookClub) {
                val intent = Intent(context, BookclubJoin::class.java)
                intent.putExtra("bookClubId", bookClub.bookClubId)
                startActivity(intent)
            }
        })

        binding.bookclubBookHomeMonthRv.adapter = bookclubByMonthRVAdapter
    }

    // 서평 상세 조회 함수(북클럽 멤버 리사이클러 뷰 클릭시)
    private fun fetchReadingRecords() {
        api.getReadingRecords().enqueue(object : Callback<ReadingRecordsListResponse> {
            override fun onResponse(call: Call<ReadingRecordsListResponse>, response: Response<ReadingRecordsListResponse>) {
                if (response.isSuccessful) {
                    val readingRecordsResponse = response.body()
                    readingRecordsResponse?.let {
                        bookclubMemberRVAdapter.setMembers(it.result.readingRecords)
                    }
                } else {
                    // 오류 처리
                }
            }

            override fun onFailure(call: Call<ReadingRecordsListResponse>, t: Throwable) {
                Log.e("BookclubBookHomeFragment", "Network Error: ${t.message}")
            }
        })
    }

    // 월별 북클럽 리스트 조회 함수
    private fun fetchBookClubMonth() {
        api.getBookClubByMonth().enqueue(object : Callback<BookClubByMonthResponse> {
            override fun onResponse(call: Call<BookClubByMonthResponse>, response: Response<BookClubByMonthResponse>) {
                if (response.isSuccessful) {
                    val bookClubMonthResponse = response.body()
                    bookClubMonthResponse?.result?.bookClubs?.let { bookClubs ->
                        bookclubByMonthRVAdapter.setBookclubByMonth(bookClubs)
                    }
                } else {
                    //오류
                }
            }

            override fun onFailure(call: Call<BookClubByMonthResponse>, t: Throwable) {
                Log.e("BookclubBookHomeFragment", "Network Error: ${t.message}")
            }
        })
    }
}