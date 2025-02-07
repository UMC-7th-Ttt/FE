package com.example.fe.bookclub_book

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fe.bookclub_book.server.BookClubReviewDetailResponse
import com.example.fe.bookclub_book.server.api
import com.example.fe.databinding.ActivityBookclubBookReviewDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookclubBookReviewDetail: AppCompatActivity() {

    private lateinit var binding: ActivityBookclubBookReviewDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookclubBookReviewDetailBinding.inflate(layoutInflater)

        binding.backBtn.setOnClickListener {
            finish()
        }

        setContentView(binding.root)

        val readingRecordId = intent.getIntExtra("readingRecordId", -1)
        if (readingRecordId != -1) {
            fetchBookClubReviewDetail(readingRecordId)
        }

//        val readingRecordId = 1
//        fetchBookClubReviewDetail(readingRecordId)
    }

    fun fetchBookClubReviewDetail(readingRecordId: Int) {
        api.getBookClubReviewDetail(readingRecordId).enqueue(object : Callback<BookClubReviewDetailResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<BookClubReviewDetailResponse>, response: Response<BookClubReviewDetailResponse>) {
                if (response.isSuccessful) {
                    val bookClubReviewDetail = response.body()
                    bookClubReviewDetail?.let {
                        binding.nicknameTv.text = it.result.memberInfo.nickname
                        binding.reviewTitleTv.text = it.result.title
                        binding.reviewMainTv.text = it.result.content
                        binding.currentPageTv.text = it.result.currentPage.toString()


                        Glide.with(this@BookclubBookReviewDetail)
                            .load(it.result.memberInfo.profileUrl)
                            .into(binding.profileIv)

//                        //리뷰 이미지 로드를 위한 코드
//                        Glide.with(this@BookclubBookReviewDetail)
//                            .load(it.result.imgUrl)
//                            .into(binding.reviewPhotoIv)


                    }
                } else {
                    // 오류 처리
                    println("Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<BookClubReviewDetailResponse>, t: Throwable) {
                println("Network Error: ${t.message}")
            }
        })
    }

}