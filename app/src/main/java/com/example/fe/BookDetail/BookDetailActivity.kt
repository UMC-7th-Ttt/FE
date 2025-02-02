package com.example.fe.BookDetail

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.BookDetail.Review.UserReview
import com.example.fe.BookDetail.Review.UserReviewAdapter
import com.example.fe.MainActivity
import com.example.fe.R

class BookDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)


        // 🔹 뒤로 가기 버튼
        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            finish()  // 현재 액티비티 종료 (이전 화면으로 이동)
        }

        // 🔹 홈 버튼
        val homeButton = findViewById<ImageView>(R.id.home_button)
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)  // 홈 화면 액티비티로 이동
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        val reviewRecyclerView = findViewById<RecyclerView>(R.id.user_rating_view)
        reviewRecyclerView.layoutManager = LinearLayoutManager(this)

        // 더미 데이터 추가
        val dummyReviews = listOf(
            UserReview(R.drawable.profile_1, "책벌레 민지", "그 아픔 속에서도, 그 사람과 함께 했던 추억들을 되새기며..."),
            UserReview(R.drawable.profile_1, "문학 소년 태준", "이 책을 읽고 난 후, 인생에 대한 시각이 완전히 달라졌다."),
            UserReview(R.drawable.profile_1, "철학자 윤아", "책 속 구절들이 하나하나 마음에 남는다. 반드시 읽어야 할 책."),
        )

        val adapter = UserReviewAdapter(dummyReviews)
        reviewRecyclerView.adapter = adapter
    }
}