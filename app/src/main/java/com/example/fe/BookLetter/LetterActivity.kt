package com.example.fe.BookLetter

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.fe.databinding.ActivityLetterBinding
import com.example.fe.network.RetrofitObj
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LetterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLetterBinding
    private lateinit var bookLetterService: BookLetterService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ ViewBinding 초기화
        binding = ActivityLetterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ Retrofit 서비스 초기화
        bookLetterService = RetrofitObj.getRetrofit().create(BookLetterService::class.java)

        // ✅ 뒤로 가기 버튼 클릭 이벤트
        binding.backButton.setOnClickListener {
            finish()
        }

        // ✅ 북레터 ID 고정 (예: 4)
        val bookLetterId = 4L


        // ✅ API 호출
        fetchBookLetterDetail(bookLetterId)
    }

    private fun fetchBookLetterDetail(bookLetterId: Long) {
        val rawToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTc0MDMxMTY3MywiZW1haWwiOiJhZG1pbjJAbmF2ZXIuY29tIn0.JwzCFHzkGRW-CESnhvcFUG6gc55MH1q10uEHvp12qubguOuKZXsQZyVrAY2mADTmwWDecC9tC5reXLh6tUR-kg" // 원래 토큰 값 (HomeFragment에서 사용한 것과 동일)
        val token = "Bearer $rawToken" // ✅ "Bearer "를 동적으로 붙여서 생성

        // ✅ 로그 추가 (Retrofit이 실제로 어떤 요청을 보내는지 확인)
        Log.d("LetterActivity", "📡 API 요청: GET /api/book-letters/$bookLetterId")
        Log.d("LetterActivity", "🔑 Authorization 헤더: $token")

        val call = bookLetterService.getBookLetterDetail(token, bookLetterId)

        call.enqueue(object : Callback<BookLetterResponse> {
            override fun onResponse(call: Call<BookLetterResponse>, response: Response<BookLetterResponse>) {
                Log.d("LetterActivity", "📡 응답 코드: ${response.code()}")
                Log.d("LetterActivity", "📡 응답 바디: ${response.body()}")

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody == null) {
                        showError("서버 응답이 비어 있습니다.")
                        return
                    }

                    val bookLetterDetail = responseBody.result
                    if (bookLetterDetail != null) {
                        bindDataToUI(bookLetterDetail)
                    } else {
                        showError("불러온 데이터가 없습니다.")
                    }
                } else {
                    Log.e("LetterActivity", "❌ 응답 실패: ${response.errorBody()?.string()}")
                    showError("북레터 데이터를 불러올 수 없습니다. (HTTP 코드: ${response.code()})")
                }
            }

            override fun onFailure(call: Call<BookLetterResponse>, t: Throwable) {
                Log.e("LetterActivity", "❌ 네트워크 오류 발생", t)
                showError("네트워크 오류 발생: ${t.message}")
            }
        })
    }

    private fun bindDataToUI(bookLetterDetail: BookLetterDetail) {
        with(binding) {
            // ✅ 배너(ViewPager2) 설정
            val bannerList = listOf(
                LetterBannerItem(
                    imageRes = bookLetterDetail.coverImg, // cover_img -> 배너 이미지 URL
                    title = bookLetterDetail.title,       // title -> 배너 제목
                    subtitle = bookLetterDetail.subtitle, // subtitle -> 배너 부제목
                    author = bookLetterDetail.editor      // editor -> 배너 작가
                )
            )
            mainBanner.adapter = LetterAdapter(bannerList)
            mainBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            // ✅ 북레터 콘텐츠 소개 바인딩 (content -> letter_intro)
            letterIntro.text = bookLetterDetail.content

            // ✅ 제공 도서 목록 바인딩 (books -> book_recycler)
            bookRecycler.layoutManager = LinearLayoutManager(this@LetterActivity)
            bookRecycler.adapter = BookAdapter(bookLetterDetail.books)
        }
    }

    private fun showError(message: String) {
        with(binding) {
            letterIntro.text = message
            mainBanner.visibility = View.GONE // 배너 숨김
            bookRecycler.visibility = View.GONE // 책 목록 숨김
        }
    }
}
