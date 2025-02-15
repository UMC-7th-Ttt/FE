package com.example.fe.BookDetail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.BookDetail.Review.UserReview
import com.example.fe.BookDetail.Review.UserReviewAdapter
import com.example.fe.MainActivity
import com.example.fe.R
import com.example.fe.databinding.ActivityBookDetailBinding

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

        // 🔹 리뷰 RecyclerView 설정
        binding.otherReviewCard.layoutManager = LinearLayoutManager(this)

        // 더미 데이터 추가
        val dummyReviews = listOf(
            UserReview(R.drawable.profile_1, "책벌레 민지", "그 아픔 속에서도, 그 사람과 함께 했던 추억들을 되새기며..."),
            UserReview(R.drawable.profile_1, "문학 소년 태준", "이 책을 읽고 난 후, 인생에 대한 시각이 완전히 달라졌다."),
            UserReview(R.drawable.profile_1, "철학자 윤아", "책 속 구절들이 하나하나 마음에 남는다. 반드시 읽어야 할 책.")
        )

        val adapter = UserReviewAdapter(dummyReviews)
        binding.otherReviewCard.adapter = adapter
    }
}
