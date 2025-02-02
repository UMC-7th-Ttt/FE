package com.example.fe.Review

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fe.R

class ReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        // UI 요소 바인딩
        val backButton = findViewById<ImageView>(R.id.back_button)
        val titleInput = findViewById<EditText>(R.id.title_input)
        val reviewInput = findViewById<EditText>(R.id.review_input)
        val charCount = findViewById<TextView>(R.id.review_char_count)
        val submitButton = findViewById<Button>(R.id.submit_button)
        val publicOption = findViewById<RadioButton>(R.id.public_option)
        val privateOption = findViewById<RadioButton>(R.id.private_option)

        // 초기 버튼 비활성화
        submitButton.isEnabled = false

        // 뒤로가기 버튼 클릭 이벤트
        backButton.setOnClickListener {
            finish() // 현재 액티비티 종료
        }

        // 서평 입력란 글자 수 표시
        reviewInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = s?.length ?: 0
                charCount.text = "$length/300" // 글자 수 업데이트
            }

            override fun afterTextChanged(s: Editable?) {
                validateForm(submitButton, titleInput, reviewInput)
            }
        })

        // 제목 입력란 변경 시 버튼 활성화 체크
        titleInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateForm(submitButton, titleInput, reviewInput)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 완료 버튼 클릭 이벤트
        submitButton.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val review = reviewInput.text.toString().trim()
            val isPublic = publicOption.isChecked // 공개 여부

            // 저장 로직 (예: SharedPreferences, DB 저장 가능)
            Toast.makeText(this, "서평이 저장되었습니다!", Toast.LENGTH_SHORT).show()

            finish() // 액티비티 종료
        }
    }

    // 제목과 서평이 입력되었는지 확인하여 버튼 활성화
    private fun validateForm(button: Button, titleInput: EditText, reviewInput: EditText) {
        val isTitleNotEmpty = titleInput.text.toString().trim().isNotEmpty()
        val isReviewNotEmpty = reviewInput.text.toString().trim().isNotEmpty()

        button.isEnabled = isTitleNotEmpty && isReviewNotEmpty
        button.setBackgroundColor(if (button.isEnabled) getColor(R.color.primary_50) else getColor(R.color.white_15))
    }
}