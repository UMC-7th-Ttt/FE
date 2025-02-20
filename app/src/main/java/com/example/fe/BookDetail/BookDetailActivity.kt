package com.example.fe.BookDetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.fe.BookDetail.Review.UserReview
import com.example.fe.BookDetail.Review.UserReviewAdapter
import com.example.fe.MainActivity
import com.example.fe.R
import com.example.fe.Review.ReviewActivity
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.databinding.ActivityBookDetailBinding
import com.example.fe.databinding.BookInfoCardBinding
import com.example.fe.databinding.FragmentScrapCancelCustomToastBinding
import com.example.fe.network.RetrofitObj
import com.example.fe.scrap.NewScrapDialogFragment
import com.example.fe.scrap.ScrapBottomSheetFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BookDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailBinding
    // 추가된 부분 !!!
    private var isBookmarked = false // 북마크 상태 저장
    private var bookId: Long = -1L // 책 ID 저장

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

        binding.icPencle.setOnClickListener {
            val intent = Intent(this, ReviewActivity::class.java)
            // 책 ID를 전달할 곳
            //intent.putExtra("BOOK_ID", bookId)
            startActivity(intent)
        }

        // ✅ 책 ID 가져오기
        val bookId = intent.getLongExtra("BOOK_ID", -1L)
        if (bookId != -1L) {
            fetchBookDetail(bookId) // API 호출
        } else {
            Log.e("BookDetailActivity", "❌ 책 ID가 없습니다.")
        }

        // 추가된 부분 !!!
        // 북마크 클릭 리스너 설정
        initBookmarkClickListener()
    }

    private fun fetchBookDetail(bookId: Long) {
        val token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTc0MDMxMTY3MywiZW1haWwiOiJhZG1pbjJAbmF2ZXIuY29tIn0.JwzCFHzkGRW-CESnhvcFUG6gc55MH1q10uEHvp12qubguOuKZXsQZyVrAY2mADTmwWDecC9tC5reXLh6tUR-kg" // 📌 실제 토큰 값 넣기

        val bookService = RetrofitObj.getRetrofit().create(BookDetailService::class.java)
        bookService.getBookDetail(token, bookId).enqueue(object : Callback<BookDetailResponse> {
            override fun onResponse(call: Call<BookDetailResponse>, response: Response<BookDetailResponse>) {
                if (response.isSuccessful) {
                    val bookDetail = response.body()?.result
                    if (bookDetail != null) {
                        // 추가된 부분 !!!!!
                        isBookmarked = bookDetail.isScraped // 북마크 상태 저장

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
            btnAuthor.text = bookDetail.author

            // ✅ 값이 없으면 버튼 숨김
            btnCategory.visibility = if (bookDetail.category.isNullOrBlank()) View.GONE else View.VISIBLE
            btnPage.visibility = if (bookDetail.itemPage == 0) View.GONE else View.VISIBLE
            btnEbook.visibility = if (bookDetail.hasEbook) View.VISIBLE else View.GONE

            // ✅ 책 표지 이미지 설정
            Glide.with(this@BookDetailActivity)
                .load(bookDetail.cover)
                .into(bookIv)


            val bookInfoCardBinding = BookInfoCardBinding.inflate(layoutInflater)
            binding.bookInfo.addView(bookInfoCardBinding.root)

            // ✅ 평점 설정
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

            // 추가된 부분 !!!
            updateBookmarkUI()
        }
    }

    // 추가된 부분 !!!!!
    private fun initBookmarkClickListener() {
        binding.icMark.setOnClickListener {
            if (isBookmarked) {
                deleteScrap()
            } else {
                val scrapBottomSheet = ScrapBottomSheetFragment(
                    bookId = bookId,
                    placeId = null,
                    onBookmarkStateChanged = { isSelected ->
                        updateBookmarkState(isSelected)
                    }
                )
                scrapBottomSheet.show(supportFragmentManager, scrapBottomSheet.tag)
            }
        }
    }

    private fun deleteScrap() {
        RetrofitClient.scrapApi.deleteBookScrap(bookId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    isBookmarked = false
                    updateBookmarkUI()

                    // ✅ 커스텀 토스트 메시지 표시
                    val inflater = LayoutInflater.from(binding.root.context)
                    val toastBinding = FragmentScrapCancelCustomToastBinding.inflate(inflater)
                    toastBinding.scrapCancelTv.text = "스크랩 취소되었습니다!"

                    val toast = Toast(binding.root.context).apply {
                        duration = Toast.LENGTH_SHORT
                        view = toastBinding.root
                        setGravity(android.view.Gravity.TOP, 0, 100)
                    }
                    toast.show()

                } else {
                    Log.e("ScrapAPI", "❌ 스크랩 취소 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ScrapAPI", "❌ 네트워크 오류: ${t.message}")
            }
        })
    }

    private fun updateBookmarkUI() {
        binding.icMark.setImageResource(
            if (isBookmarked) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
        )
    }

    private fun updateBookmarkState(isSelected: Boolean) {
        isBookmarked = isSelected
        updateBookmarkUI()
    }
}
