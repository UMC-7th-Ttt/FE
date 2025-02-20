package com.example.fe.Review

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fe.databinding.ActivityReviewSpaceBinding

class SpaceReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewSpaceBinding
    private var placeId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewSpaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔹 인텐트에서 데이터 가져오기
        placeId = intent.getLongExtra("PLACE_ID", -1L)
        val placeTitle = intent.getStringExtra("PLACE_TITLE") ?: "장소 없음"
        val placeImage = intent.getStringExtra("PLACE_IMAGE") ?: ""

        // 🔹 로그 추가 (Intent 값 확인)
        android.util.Log.d("SpaceReviewActivity", "Received Intent Data:")
        android.util.Log.d("SpaceReviewActivity", "PLACE_ID: $placeId")
        android.util.Log.d("SpaceReviewActivity", "PLACE_TITLE: $placeTitle")
        android.util.Log.d("SpaceReviewActivity", "PLACE_IMAGE: $placeImage")

        // 🔹 UI 적용
        binding.titleText.text = placeTitle
        Glide.with(this).load(placeImage).into(binding.bookImage)

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

        // 🔹 완료 버튼 클릭 시 ReviewActivity로 이동
        binding.submitButton.setOnClickListener {
            val rating = binding.ratingBar.rating

            // 🔹 로그 추가 (Intent 보내기 전 데이터 확인)
            android.util.Log.d("SpaceReviewActivity", "Sending Intent Data:")
            android.util.Log.d("SpaceReviewActivity", "PLACE_ID: $placeId")
            android.util.Log.d("SpaceReviewActivity", "PLACE_TITLE: $placeTitle")
            android.util.Log.d("SpaceReviewActivity", "PLACE_IMAGE: $placeImage")
            android.util.Log.d("SpaceReviewActivity", "PLACE_RATING: $rating")

            val intent = Intent(this, ReviewActivity::class.java).apply {
                putExtra("PLACE_ID", placeId)
                putExtra("PLACE_TITLE", placeTitle)
                putExtra("PLACE_IMAGE", placeImage)
                putExtra("PLACE_RATING", rating)
            }
            startActivity(intent)  // ✅ 기존 ReviewActivity가 있다면 재사용
            finish()  // 현재 액티비티 종료
        }
    }

    // ✅ 별점이 0.5 이상이면 버튼 활성화
    private fun validateForm(rating: Float) {
        binding.submitButton.isEnabled = rating >= 0.5
    }
}
