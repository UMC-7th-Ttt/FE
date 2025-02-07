package com.example.fe.bookclub_book

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.fe.bookclub_book.adapter.BookclubBookDetailMemberRVAdapter
import com.example.fe.bookclub_book.server.BookClubDetailResponse
import com.example.fe.bookclub_book.server.api
import com.example.fe.databinding.ActivityBookclubBookDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookclubBookDetail: AppCompatActivity() {

    private lateinit var binding: ActivityBookclubBookDetailBinding
    private lateinit var bookclubDetailMemberRVAdapter: BookclubBookDetailMemberRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookclubBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        // 인증하기 버튼 클릭 리스너
        binding.certifyBtn.setOnClickListener {
            val intent = Intent(this, BookclubWriteReview::class.java)
            startActivity(intent)
        }

        // 임의 id로 설정
        val bookClubId = 1
        fetchBookClubDetail(bookClubId)

        initBookclubDetailMemberRecyclerview()
    }

    // 북클럽 상세 화면 조회 함수
    fun fetchBookClubDetail(bookClubId: Int) {
        api.getBookClubDetail(bookClubId).enqueue(object : Callback<BookClubDetailResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<BookClubDetailResponse>, response: Response<BookClubDetailResponse>) {
                if (response.isSuccessful) {
                    val bookclubDetail = response.body()
                    bookclubDetail?.let {
                        binding.bookTitleTv.text = it.result.bookInfo.title
                        binding.bookAuthorTv.text = it.result.bookInfo.author
                        binding.publisherTv.text = it.result.bookInfo.publisher
                        binding.myCompletionProgressBar.progress = it.result.myCompletionRate
                        binding.myCompletionProgressTv.text = "${it.result.myCompletionRate}%"
                        binding.recommendCompletionProgressBar.progress = it.result.recommendedCompletionRate
                        binding.recommendCompletionProgressTv.text = "${it.result.recommendedCompletionRate}%"
                        binding.elapseWeekTv.text = it.result.elapsedWeeks.toString()

                        Glide.with(this@BookclubBookDetail)
                            .load(it.result.bookInfo.cover)
                            .into(binding.bookIv)

                        Glide.with(this@BookclubBookDetail)
                            .load(it.result.bookInfo.cover)
                            .into(binding.bookBgIv)

                        bookclubDetailMemberRVAdapter.setMembers(it.result.members)

                        // 인증 버튼의 visibility 설정
                        if (it.result.elapsedWeeks == 4) {
                            binding.certifyBtn.visibility = View.VISIBLE
                        } else {
                            binding.certifyBtn.visibility = View.INVISIBLE
                        }

                    }
                } else {
                    // 오류 처리
                    println("Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<BookClubDetailResponse>, t: Throwable) {
                println("Network Error: ${t.message}")
            }
        })
    }

    // 북클럽 상세 화면 멤버 조회 함수
    private fun initBookclubDetailMemberRecyclerview() {
        binding.bookclubDetailMemberRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        bookclubDetailMemberRVAdapter = BookclubBookDetailMemberRVAdapter()
        binding.bookclubDetailMemberRv.adapter = bookclubDetailMemberRVAdapter
    }

}