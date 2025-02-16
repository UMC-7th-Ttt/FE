package com.example.fe.BookDetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.fe.BookDetail.Review.UserReview
import com.example.fe.BookDetail.Review.UserReviewAdapter
import com.example.fe.MainActivity
import com.example.fe.R
import com.example.fe.databinding.ActivityBookDetailBinding
import com.example.fe.databinding.BookInfoCardBinding
import com.example.fe.network.RetrofitObj
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BookDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ ViewBinding 초기화
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ 툴바 설정 (뒤로 가기 버튼 추가)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로 가기 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_prev_arrow) // 커스텀 아이콘 설정

        // 🔹 툴바의 뒤로 가기 버튼 클릭 시 액티비티 종료
        binding.toolbar.setNavigationOnClickListener {
            finish()  // 현재 액티비티 종료 (이전 화면으로 이동)
        }

        // 🔹 홈 버튼
        binding.homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)  // 홈 화면 액티비티로 이동
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        // ✅ 책 ID 가져오기
        val bookId = intent.getLongExtra("BOOK_ID", -1L)
        if (bookId != -1L) {
            fetchBookDetail(bookId) // API 호출
        } else {
            Log.e("BookDetailActivity", "❌ 책 ID가 없습니다.")
        }
    }

    private fun fetchBookDetail(bookId: Long) {
        val token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9..." // 📌 실제 토큰 값 넣기

        val bookService = RetrofitObj.getRetrofit().create(BookDetailService::class.java)
        bookService.getBookDetail(token, bookId).enqueue(object : Callback<BookDetailResponse> {
            override fun onResponse(call: Call<BookDetailResponse>, response: Response<BookDetailResponse>) {
                if (response.isSuccessful) {
                    val bookDetail = response.body()?.result
                    if (bookDetail != null) {
                        bindDataToUI(bookDetail) // 📌 UI 업데이트
                    }
                } else {
                    Log.e("BookDetailActivity", "❌ 응답 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<BookDetailResponse>, t: Throwable) {
                Log.e("BookDetailActivity", "❌ 네트워크 오류 발생", t)
            }
        })
    }

    private fun bindDataToUI(bookDetail: BookDetail) {
        with(binding) {
            // ✅ 책 기본 정보 설정
            bookTitleTv.text = bookDetail.title
            bookAuthorTv.text = bookDetail.author
            publisherTv.text = bookDetail.publisher
            bookExcerpt.text = bookDetail.description
            btnCategory.text = bookDetail.category
            btnPage.text = "${bookDetail.itemPage}쪽"
            btnEbook.text = "E북 등록"

            // ✅ 값이 없으면 버튼 숨김
            btnCategory.visibility = if (bookDetail.category.isNullOrBlank()) View.GONE else View.VISIBLE
            btnPage.visibility = if (bookDetail.itemPage == 0) View.GONE else View.VISIBLE
            btnEbook.visibility = if (bookDetail.hasEbook) View.VISIBLE else View.GONE

            // ✅ 책 표지 이미지 설정
            Glide.with(this@BookDetailActivity)
                .load(bookDetail.cover)
                .into(bookIv)

            // ✅ book_info_card.xml의 바인딩 객체 가져오기 (이름 변경 반영)
            val bookInfoCardBinding = BookInfoCardBinding.bind(binding.bookInfo)

            // ✅ book_info_card 내부의 RatingBar 설정
            bookInfoCardBinding.similarUsersRatingBar.rating = bookDetail.userRating.toFloat()
            bookInfoCardBinding.overallRatingBar.rating = bookDetail.totalRating.toFloat()


            // ✅ 리뷰 목록 설정
            if (bookDetail.reviews.isNotEmpty()) {
                val reviews = bookDetail.reviews.map {
                    UserReview(
                        profileImage = R.drawable.profile_1, // Glide로 설정해야 함
                        userName = it.memberInfo.nickname,
                        reviewText = it.content
                    )
                }
                val adapter = UserReviewAdapter(reviews)
                otherReviewCard.layoutManager = LinearLayoutManager(this@BookDetailActivity)
                otherReviewCard.adapter = adapter
            } else {
                otherReview.visibility = View.GONE // 리뷰 없으면 제목 숨김
            }
        }
    }
}
