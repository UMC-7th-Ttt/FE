package com.example.fe.Review

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fe.R

class BookReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_book)

        val backButton = findViewById<ImageView>(R.id.back_button)
        val ratingBar = findViewById<RatingBar>(R.id.rating_bar)
        val ratingText = findViewById<EditText>(R.id.rating_text) // EditText로 변경
        val submitButton = findViewById<Button>(R.id.submit_button)

        // 🔹 뒤로 가기 버튼 클릭 시, ReviewActivity로 이동
        backButton.setOnClickListener {
            navigateToReviewActivity()
        }

        // 초기 상태: 버튼 비활성화
        submitButton.isEnabled = false

        // ⭐ 별점이 변경될 때 EditText도 업데이트
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            ratingText.setText(String.format("%.1f", rating)) // 숫자 표시
            validateForm(submitButton, rating) // 버튼 활성화 확인
        }

        // ⭐ EditText에서 숫자를 입력하면 별점도 변경되도록 설정
        ratingText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val rating = s.toString().toFloatOrNull() ?: 0f
                if (rating in 0.0..5.0) {
                    ratingBar.rating = rating // 별점 UI도 업데이트
                    validateForm(submitButton, rating) // 버튼 활성화 확인
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 🔹 완료 버튼 클릭 시, ReviewActivity로 이동
        submitButton.setOnClickListener {
            Toast.makeText(this, "서평이 저장되었습니다!", Toast.LENGTH_SHORT).show()
            navigateToReviewActivity()
        }
    }

    // ⭐ ReviewActivity로 이동하는 함수
    private fun navigateToReviewActivity() {
        val intent = Intent(this, ReviewActivity::class.java)
        startActivity(intent)
        finish() // 현재 BookReviewActivity 종료
    }

    // 별점이 0.5 이상이면 버튼 활성화
    private fun validateForm(button: Button, rating: Float) {
        button.isEnabled = rating > 0.0
        button.setBackgroundColor(if (button.isEnabled) getColor(R.color.primary_50) else getColor(R.color.white_15))
    }
}