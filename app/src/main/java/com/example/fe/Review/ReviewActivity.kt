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



    //공간아이템추가어텝터
    private lateinit var placeReviewAdapter: PlaceReviewAdapter
    private val placeList = mutableListOf<PlaceReviewItem>()

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

        //공간리사이클러뷰설정
        placeReviewAdapter = PlaceReviewAdapter(placeList)
        binding.additionalRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ReviewActivity)
            adapter = placeReviewAdapter
        }

        // ✅ 도서 / 장소 추가 버튼 클릭 → `SearchMainActivity`로 이동
        binding.addLocationButton.setOnClickListener {
            val intent = Intent(this, SearchMainActivity::class.java)
            intent.putExtra("CALLER", "ReviewActivity")
            startActivity(intent)
            //finish()
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
        setIntent(intent) // ✅ 새로운 Intent를 저장
        checkForNewBook()
        checkForNewPlace()

    }

    // ✅ `Intent`에서 데이터 가져와서 RecyclerView에 추가
    private fun checkForNewBook() {

        bookList.clear()//기존데이터 초기화
        reviewBookAdapter.notifyDataSetChanged() // RecyclerView 갱신

        val bookId = intent.getLongExtra("BOOK_ID", -1)
        val bookTitle = intent.getStringExtra("BOOK_TITLE")
        val bookCover = intent.getStringExtra("BOOK_COVER")
        val bookRating = intent.getFloatExtra("BOOK_RATING", 0f)

        android.util.Log.d("ReviewActivity", "checkForNewBook() called")
        android.util.Log.d("ReviewActivity", "BOOK_ID: $bookId")
        android.util.Log.d("ReviewActivity", "BOOK_TITLE: $bookTitle")
        android.util.Log.d("ReviewActivity", "BOOK_COVER: $bookCover")
        android.util.Log.d("ReviewActivity", "BOOK_RATING: $bookRating")

        if (bookId != -1L && bookTitle != null && bookCover != null) {
            val newBook = ReviewItem(bookTitle, "작가 미상", bookCover)

            //bookList.add(newBook)
            addBookToRecyclerView(newBook)

            // ✅ 한 번만 추가되도록 `Intent` 데이터 제거
            intent.removeExtra("BOOK_ID")
            intent.removeExtra("BOOK_TITLE")
            intent.removeExtra("BOOK_COVER")
            intent.removeExtra("BOOK_RATING")
        }
    }

    //장소추가
    private fun checkForNewPlace() {
        val placeId = intent.getLongExtra("PLACE_ID", -1)
        val placeTitle = intent.getStringExtra("PLACE_TITLE")
        val placeImage = intent.getStringExtra("PLACE_IMAGE")

        if (placeId != -1L && placeTitle != null && placeImage != null) {
            val newPlace = PlaceReviewItem(placeTitle, "위치 정보 없음", placeImage)
            //placeReviewAdapter.addPlace(newPlace)
            addPlaceToRecyclerView(newPlace)

            // ✅ 한 번만 추가되도록 `Intent` 데이터 제거
            intent.removeExtra("PLACE_ID")
            intent.removeExtra("PLACE_TITLE")
            intent.removeExtra("PLACE_IMAGE")
        }
    }

    // ✅ RecyclerView에 아이템 추가
    private fun addBookToRecyclerView(book: ReviewItem) {
        bookList.add(book)
        reviewBookAdapter.notifyItemInserted(bookList.size - 1)
    }

    //공간리사이클러뷰 추가
    private fun addPlaceToRecyclerView(place: PlaceReviewItem) {
        placeList.add(place)
        placeReviewAdapter.notifyItemInserted(placeList.size - 1)
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
