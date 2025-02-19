package com.example.fe.Review

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fe.databinding.ActivityReviewBookBinding

class BookReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBookBinding
    private var bookId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔹 인텐트에서 데이터 가져오기
        bookId = intent.getIntExtra("BOOK_ID", -1)
        val bookTitle = intent.getStringExtra("BOOK_TITLE") ?: "제목 없음"
        val bookCover = intent.getStringExtra("BOOK_COVER") ?: ""

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
            saveBookToPreferences(bookId, bookTitle, bookCover, binding.ratingBar.rating)

            val intent = Intent(this, ReviewActivity::class.java)
            startActivity(intent)  // ✅ ReviewActivity로 이동
            finish()  // 현재 액티비티 종료
        }
    }

    // ✅ 별점이 0.5 이상이면 버튼 활성화
    private fun validateForm(rating: Float) {
        binding.submitButton.isEnabled = rating >= 0.5
    }

    // ✅ SharedPreferences에 데이터 저장
    private fun saveBookToPreferences(bookId: Int, bookTitle: String, bookCover: String, rating: Float) {
        val sharedPref = getSharedPreferences("ReviewData", MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putInt("BOOK_ID", bookId)
        editor.putString("BOOK_TITLE", bookTitle)
        editor.putString("BOOK_COVER", bookCover)
        editor.putFloat("BOOK_RATING", rating)
        editor.apply()
    }
}
