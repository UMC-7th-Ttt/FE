package com.example.fe.Review

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import com.example.fe.R

class SpaceReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_space)  // 공간별점 레이아웃

        val backButton = findViewById<ImageView>(R.id.back_button)
        val ratingBar = findViewById<RatingBar>(R.id.rating_bar)
        val ratingText = findViewById<EditText>(R.id.rating_text)
        val submitButton = findViewById<Button>(R.id.submit_button)

        // 초기 버튼 비활성화
        submitButton.isEnabled = false

        // 🔹 뒤로가기 버튼 클릭 시 ReviewActivity로 이동
        backButton.setOnClickListener {
            val intent = Intent(this, ReviewActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 🔹 별점이 변경될 때 숫자 업데이트
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            ratingText.setText(String.format("%.1f", rating))
            submitButton.isEnabled = rating > 0.0
        }

        // 🔹 숫자 입력 시 별점 업데이트
        ratingText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val ratingValue = s.toString().toFloatOrNull() ?: 0.0f
                if (ratingValue in 0.0..5.0) {
                    ratingBar.rating = ratingValue
                    submitButton.isEnabled = ratingValue > 0.0
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 🔹 완료 버튼 클릭 시 ReviewActivity로 이동
        submitButton.setOnClickListener {
            val intent = Intent(this, ReviewActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}