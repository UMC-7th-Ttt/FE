package com.example.fe.Review

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fe.R
import com.example.fe.databinding.ActivityReviewBookBinding

class BookReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBookBinding
    private var bookId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔹 인텐트에서 데이터 가져오기
        bookId = intent.getLongExtra("BOOK_ID", -1)
        val bookTitle = intent.getStringExtra("BOOK_TITLE") ?: "제목 없음"
        val bookCover = intent.getStringExtra("BOOK_COVER") ?: ""
        val bookAuthor = intent.getStringExtra("BOOK_AUTHOR") ?: "저자 없음"

        // 📌 로그 추가
        Log.d("BookReviewActivity", "Intent received in BookReviewActivity")
        Log.d("BookReviewActivity", "BOOK_ID: $bookId")
        Log.d("BookReviewActivity", "BOOK_TITLE: $bookTitle")
        Log.d("BookReviewActivity", "BOOK_COVER: $bookCover")

        // 🔹 UI 적용
        binding.bookTitle.text = bookTitle
        Glide.with(this).load(bookCover).into(binding.bookImage)

        // ✅ 초기 상태: 버튼 비활성화
        binding.submitButton.isEnabled = false

        // ⭐ 별점 입력 시 업데이트 및 버튼 활성화 체크
        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            binding.ratingText.setText(String.format("%.1f", rating))
            validateForm(rating)
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        // ⭐ EditText에서 숫자를 입력하면 별점도 변경되도록 설정
        binding.ratingText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val rating = s.toString().toFloatOrNull() ?: 0f
                if (rating in 0.0..5.0) {
                    binding.ratingBar.rating = rating // 별점 UI도 업데이트
                    validateForm(rating)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 🔹 완료 버튼 클릭 시 데이터 저장 후 ReviewActivity로 이동
        binding.submitButton.setOnClickListener {
            val rating = binding.ratingBar.rating
            saveBookToPreferences(bookId, bookTitle, bookAuthor, bookCover, rating) // ✅ SharedPreferences 저장

            val intent = Intent(this, ReviewActivity::class.java)
            startActivity(intent)  // ✅ ReviewActivity로 이동

            // 📌 완료 버튼 눌렀을 때 로그 추가
            Log.d("BookReviewActivity", "Book data saved to SharedPreferences")
            Log.d("BookReviewActivity", "Navigating to ReviewActivity")

            finish()  // 현재 액티비티 종료
        }
    }

    // ✅ SharedPreferences에 데이터 저장
    private fun saveBookToPreferences(bookId: Long, bookTitle: String, bookAuthor: String, bookCover: String, rating: Float) {
        val sharedPref = getSharedPreferences("ReviewData", MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putLong("BOOK_ID", bookId)
        editor.putString("BOOK_TITLE", bookTitle)
        editor.putString("BOOK_AUTHOR", bookAuthor)
        editor.putString("BOOK_COVER", bookCover)
        editor.putFloat("BOOK_RATING", rating)
        editor.apply() // 변경 사항 저장
    }

    // ✅ 별점이 0.5 이상이면 버튼 활성화
    private fun validateForm(rating: Float) {
        binding.submitButton.isEnabled = rating >= 0.5

        // 활성화 상태일 때 색상 변경
        binding.submitButton.setBackgroundColor(
            if (rating >= 0.5) getColor(R.color.primary_50) else getColor(R.color.white_10)
        )




        // 활성화 상태일 때 색상 변경
       /* binding.submitButton.setBackgroundColor(
            if (rating >= 0.5) getColor(R.color.primary_50) else getColor(R.color.white_10)
        )*/
    }
}
