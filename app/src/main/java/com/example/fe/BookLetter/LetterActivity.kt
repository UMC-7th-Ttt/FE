package com.example.fe.BookLetter

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.fe.R

class LetterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letter)

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            finish() // 현재 액티비티 종료 → 이전 화면으로 돌아감
        }


        // 📌 배너 ViewPager2 설정
        val viewPager = findViewById<ViewPager2>(R.id.main_banner)
        viewPager.adapter = ViewPagerAdapter(getBannerList())
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 📌 리사이클러뷰 설정
        val recyclerView = findViewById<RecyclerView>(R.id.book_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = BookAdapter(getBookList())
    }

    // 📌 배너 데이터 추가
    private fun getBannerList(): List<LetterBannerItem> {
        return listOf(
            LetterBannerItem(R.drawable.viewpager_sample2, "삶에 대한 고찰", "청춘이 말하는 삶의 의미", "파묵"),
            LetterBannerItem(R.drawable.viewpager_sample2, "행복의 철학", "작은 것에서 찾는 행복", "알랭 드 보통"),
            LetterBannerItem(R.drawable.viewpager_sample2, "자기 계발의 힘", "성공하는 사람들의 비밀", "브라이언 트레이시")
        )
    }

    // 📌 책 목록 데이터 추가
    private fun getBookList(): List<Book> {
        return listOf(
            Book("책 제목 1", "저자1", "출판사1", R.drawable.book_sample1),
            Book("책 제목 2", "저자2", "출판사2", R.drawable.book_sample1)
        )
    }
}
