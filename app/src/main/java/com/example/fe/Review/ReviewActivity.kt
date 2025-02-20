package com.example.fe.Review

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.R
import com.example.fe.databinding.ActivityReviewBinding
import com.example.fe.search.SearchMainActivity

class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding
    private lateinit var reviewBookAdapter: ReviewBookAdapter
    private val bookList = mutableListOf<ReviewItem>() // ✅ 도서 리스트

    private lateinit var placeReviewAdapter: PlaceReviewAdapter
    private val placeList = mutableListOf<PlaceReviewItem>() // ✅ 장소 리스트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ 도서 RecyclerView 설정
        reviewBookAdapter = ReviewBookAdapter(bookList)
        binding.locationRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ReviewActivity)
            adapter = reviewBookAdapter
        }

        // ✅ 장소 RecyclerView 설정
        placeReviewAdapter = PlaceReviewAdapter(placeList)
        binding.additionalRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ReviewActivity)
            adapter = placeReviewAdapter
        }

        // ✅ 도서 / 장소 추가 버튼 클릭
        binding.addLocationButton.setOnClickListener {
            val intent = Intent(this, SearchMainActivity::class.java)
            intent.putExtra("CALLER", "ReviewActivity")
            startActivity(intent)
        }

        // ✅ 완료 버튼 클릭 시
        binding.submitButton.setOnClickListener {
            Toast.makeText(this, "서평이 저장되었습니다!", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.reviewInput.addTextChangedListener(textWatcher)
        binding.titleInput.addTextChangedListener(textWatcher)
    }

    override fun onResume() {
        super.onResume()
        loadPlaceFromPreferences() // ✅ 장소 데이터 불러오기
    }

    // ✅ `SharedPreferences`에서 장소 데이터 불러오기
    private fun loadPlaceFromPreferences() {
        val sharedPref = getSharedPreferences("ReviewData", MODE_PRIVATE)
        val placeId = sharedPref.getLong("PLACE_ID", -1)
        val placeTitle = sharedPref.getString("PLACE_TITLE", null)
        val placeImage = sharedPref.getString("PLACE_IMAGE", null)

        if (placeId != -1L && placeTitle != null && placeImage != null) {
            val newPlace = PlaceReviewItem(placeTitle, "위치 정보 없음", placeImage)

            placeList.clear() // 기존 데이터 초기화
            placeList.add(newPlace)
            placeReviewAdapter.notifyDataSetChanged()
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            validateForm()
        }
        override fun afterTextChanged(s: Editable?) {}
    }

    private fun validateForm() {
        val isTitleNotEmpty = binding.titleInput.text.toString().trim().isNotEmpty()
        val isReviewNotEmpty = binding.reviewInput.text.toString().trim().isNotEmpty()
        val isFormValid = isTitleNotEmpty && isReviewNotEmpty

        binding.submitButton.isEnabled = isFormValid
        binding.submitButton.setBackgroundColor(
            if (isFormValid) getColor(R.color.primary_50) else getColor(R.color.white_10)
        )
    }
}
