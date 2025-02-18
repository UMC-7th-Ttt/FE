package com.example.fe.Review

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fe.R
import com.example.fe.databinding.ActivityReviewBinding

class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기 버튼 비활성화
        binding.submitButton.isEnabled = false

        // 뒤로가기 버튼 클릭 이벤트
        binding.backButton.setOnClickListener {
            finish() // 현재 액티비티 종료
        }

        // 서평 입력란 글자 수 표시
        binding.reviewInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = s?.length ?: 0
                binding.reviewCharCount.text = "$length/300" // 글자 수 업데이트
            }

            override fun afterTextChanged(s: Editable?) {
                validateForm()
            }
        })

        // 제목 입력란 변경 시 버튼 활성화 체크
        binding.titleInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateForm()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 완료 버튼 클릭 이벤트
        binding.submitButton.setOnClickListener {
            val title = binding.titleInput.text.toString().trim()
            val review = binding.reviewInput.text.toString().trim()
            val isPublic = binding.publicOption.isChecked // 공개 여부

            // 저장 로직 (예: SharedPreferences, DB 저장 가능)
            Toast.makeText(this, "서평이 저장되었습니다!", Toast.LENGTH_SHORT).show()

            finish() // 액티비티 종료
        }
    }

    // 제목과 서평이 입력되었는지 확인하여 버튼 활성화
    private fun validateForm() {
        val isTitleNotEmpty = binding.titleInput.text.toString().trim().isNotEmpty()
        val isReviewNotEmpty = binding.reviewInput.text.toString().trim().isNotEmpty()

        binding.submitButton.isEnabled = isTitleNotEmpty && isReviewNotEmpty
        binding.submitButton.setBackgroundColor(
            if (binding.submitButton.isEnabled) getColor(R.color.primary_50) else getColor(R.color.white_10)
        )
    }
}
