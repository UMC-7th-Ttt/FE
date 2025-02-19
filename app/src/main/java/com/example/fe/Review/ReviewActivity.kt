package com.example.fe.Review

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.databinding.ActivityReviewBinding
import com.example.fe.search.SearchMainActivity

class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding
    private lateinit var reviewBookAdapter: ReviewBookAdapter
    private val bookList = mutableListOf<ReviewItem>() // ✅ 리스트 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ 앱 실행 시 SharedPreferences 데이터 초기화 (이전 기록 삭제)
        clearSavedReviews()

        // 초기 버튼 비활성화
        binding.submitButton.isEnabled = false

        // 뒤로가기 버튼 클릭 이벤트
        binding.backButton.setOnClickListener {
            finish() // 현재 액티비티 종료
        }

        // ✅ RecyclerView 설정
        reviewBookAdapter = ReviewBookAdapter(bookList) // ✅ 어댑터 초기화
        binding.locationRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ReviewActivity)
            adapter = reviewBookAdapter
        }

        // addLocationButton 클릭 시 SearchMainActivity로 이동
        binding.addLocationButton.setOnClickListener {
            val intent = Intent(this, SearchMainActivity::class.java).apply {
                putExtra("CALLER", "ReviewActivity") // 호출한 액티비티 정보 추가
            }
            startActivity(intent)
        }

        // ✅ SharedPreferences에서 데이터 불러오기
        loadSavedReviews()

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

    override fun onResume() {
        super.onResume()
        loadSavedReviews() // ✅ 액티비티가 다시 실행될 때 데이터 로드
    }

    // ✅ 앱 실행 시 저장된 리뷰 데이터 초기화 (이전 기록 삭제)
    private fun clearSavedReviews() {
        val sharedPref = getSharedPreferences("ReviewData", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear() // 모든 데이터 삭제
        editor.apply()
    }

    // ✅ SharedPreferences에서 데이터 불러오기
    private fun loadSavedReviews() {
        val sharedPref = getSharedPreferences("ReviewData", MODE_PRIVATE)

        // ✅ 기존 리스트 초기화 (중복 방지)
        bookList.clear()
        reviewBookAdapter.notifyDataSetChanged()

        if (sharedPref.contains("BOOK_ID")) {
            val bookItem = ReviewItem(
                bookTitle = sharedPref.getString("BOOK_TITLE", "제목 없음") ?: "제목 없음",
                author = "책 저자",  // API 연동 시 수정 가능
                coverUrl = sharedPref.getString("BOOK_COVER", "") ?: ""
            )

            // ✅ 중복 확인 후 추가
            if (!bookList.contains(bookItem)) {
                addBookToRecyclerView(bookItem)
            }
        }

        if (sharedPref.contains("PLACE_ID")) {
            val placeItem = ReviewItem(
                bookTitle = sharedPref.getString("PLACE_TITLE", "장소 없음") ?: "장소 없음",
                author = "공간",
                coverUrl = sharedPref.getString("PLACE_IMAGE", "") ?: ""
            )

            // ✅ 중복 확인 후 추가
            if (!bookList.contains(placeItem)) {
                addBookToRecyclerView(placeItem)
            }
        }
    }

    // ✅ 책 추가 메서드
    private fun addBookToRecyclerView(book: ReviewItem) {
        bookList.add(book)
        reviewBookAdapter.notifyItemInserted(bookList.size - 1)
    }

    // 제목과 서평이 입력되었는지 확인하여 버튼 활성화
    private fun validateForm() {
        val isTitleNotEmpty = binding.titleInput.text.toString().trim().isNotEmpty()
        val isReviewNotEmpty = binding.reviewInput.text.toString().trim().isNotEmpty()

        binding.submitButton.isEnabled = isTitleNotEmpty && isReviewNotEmpty
    }
}
