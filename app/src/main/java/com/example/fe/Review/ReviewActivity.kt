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
    private val bookList = mutableListOf<ReviewItem>() // ✅ 리스트 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ RecyclerView 설정
        reviewBookAdapter = ReviewBookAdapter(bookList)
        binding.locationRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ReviewActivity)
            adapter = reviewBookAdapter
        }

        // ✅ 도서 / 장소 추가 버튼 클릭 → `SearchMainActivity`로 이동
        binding.addLocationButton.setOnClickListener {
            val intent = Intent(this, SearchMainActivity::class.java)
            intent.putExtra("CALLER", "ReviewActivity")
            startActivity(intent)
            finish()
        }

        // ✅ 완료 버튼 클릭 시
        binding.submitButton.setOnClickListener {
            Toast.makeText(this, "서평이 저장되었습니다!", Toast.LENGTH_SHORT).show()
            finish()
        }

        // ✅ 뒤로 가기 버튼 클릭 (초기화 후 `MyPageFragment`로 이동)
        binding.backButton.setOnClickListener {


            finish() // ✅ `ReviewActivity` 종료
        }

        // ✅ 제목 및 서평 입력 감지하여 버튼 활성화
        binding.reviewInput.addTextChangedListener(textWatcher)
        binding.titleInput.addTextChangedListener(textWatcher)
    }

    override fun onResume() {
        super.onResume()

    }

    // ✅ `Intent`에서 데이터 가져와서 RecyclerView에 추가
    private fun checkForNewBook() {
        val bookId = intent.getIntExtra("BOOK_ID", -1)
        val bookTitle = intent.getStringExtra("BOOK_TITLE")
        val bookCover = intent.getStringExtra("BOOK_COVER")
        val bookRating = intent.getFloatExtra("BOOK_RATING", 0f)

        if (bookId != -1 && bookTitle != null && bookCover != null) {
            val newBook = ReviewItem(bookTitle, "작가 미상", bookCover)
            addBookToRecyclerView(newBook)

            // ✅ 한 번만 추가되도록 `Intent` 데이터 제거
            intent.removeExtra("BOOK_ID")
            intent.removeExtra("BOOK_TITLE")
            intent.removeExtra("BOOK_COVER")
            intent.removeExtra("BOOK_RATING")
        }
    }

    // ✅ RecyclerView에 아이템 추가
    private fun addBookToRecyclerView(book: ReviewItem) {
        bookList.add(book)
        reviewBookAdapter.notifyItemInserted(bookList.size - 1)
    }

    // ✅ 제목과 서평이 입력되었는지 확인하여 버튼 활성화
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
