package com.example.fe.Review

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.JohnRetrofitClient
import com.example.fe.MainActivity
import com.example.fe.R
import com.example.fe.databinding.ActivityReviewBinding
import com.example.fe.search.SearchMainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding
    private lateinit var reviewBookAdapter: ReviewBookAdapter
    private val bookList = mutableListOf<ReviewItem>() // ✅ 도서 리스트

    private lateinit var placeReviewAdapter: PlaceReviewAdapter
    private val placeList = mutableListOf<PlaceReviewItem>() // ✅ 장소 리스트

    private lateinit var apiService: ReviewApiService//api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ 최신 방식의 백버튼 핸들링
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                clearReviewPreferences() // ✅ SharedPreferences 초기화
                finish() // 🔙 액티비티 종료
            }
        })

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
            //Toast.makeText(this, "서평이 저장되었습니다!", Toast.LENGTH_SHORT).show()
            submitReview()

            navigateToMyPage()

            //finish()
        }

        // API 서비스 초기화
        apiService = JohnRetrofitClient.getClient(this).create(ReviewApiService::class.java)


        binding.backButton.setOnClickListener {
            finish()
        }

        binding.reviewInput.addTextChangedListener(textWatcher)
        binding.titleInput.addTextChangedListener(textWatcher)
    }


    //api전송
    private fun submitReview() {
        val sharedPref = getSharedPreferences("ReviewData", MODE_PRIVATE)
        val title = binding.titleInput.text.toString()
        val content = binding.reviewInput.text.toString()
        val bookRanking = sharedPref.getFloat("BOOK_RATING", 0f)
        val placeRanking = sharedPref.getFloat("PLACE_RATING", 0f)
        val isSecret = binding.privateOption.isChecked
        val bookId = sharedPref.getLong("BOOK_ID", -1L).takeIf { it != -1L }
        val placeId = sharedPref.getLong("PLACE_ID", -1L).takeIf { it != -1L }

        // 현재 날짜 가져오기 (형식: YYYY-MM-DD)
        val writeDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // 현재 날짜 대신 하루 전 날짜로 설정


        val reviewRequest = ReviewRequest(
            title = title,
            content = content,
            bookRanking = bookRanking,
            placeRanking = placeRanking,
            isSecret = isSecret,
            writeDate = writeDate,
            bookId = bookId,
            placeId = placeId
        )


        android.util.Log.d("ReviewAPI", "📌 API 요청 데이터:")
        android.util.Log.d("ReviewAPI", "title: $title")
        android.util.Log.d("ReviewAPI", "content: $content")
        android.util.Log.d("ReviewAPI", "bookRanking: $bookRanking")
        android.util.Log.d("ReviewAPI", "placeRanking: $placeRanking")
        android.util.Log.d("ReviewAPI", "isSecret: $isSecret")
        android.util.Log.d("ReviewAPI", "writeDate: $writeDate")
        android.util.Log.d("ReviewAPI", "bookId: $bookId")
        android.util.Log.d("ReviewAPI", "placeId: $placeId")


        apiService.submitReview(reviewRequest).enqueue(object : Callback<ReviewResponse> {
            override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                android.util.Log.d("ReviewAPI", "🔵 응답 코드: ${response.code()}")
                android.util.Log.d("ReviewAPI", "🔵 응답 메시지: ${response.message()}")
                android.util.Log.d("ReviewAPI", "🔵 응답 바디: ${response.errorBody()?.string()}")

                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    Toast.makeText(this@ReviewActivity, "서평이 성공적으로 등록되었습니다!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@ReviewActivity, "서평 등록 실패: ${response.body()?.message}", Toast.LENGTH_LONG).show()
                }
            }


            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                Toast.makeText(this@ReviewActivity, "네트워크 오류 발생", Toast.LENGTH_LONG).show()
            }
        })


    }
    // ✅ 마이페이지로 이동하는 함수
    private fun navigateToMyPage() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("FRAGMENT", "MyPageFragment") // ✅ 마이페이지로 이동
        }
        startActivity(intent)
        finish() // ✅ 현재 ReviewActivity 종료
    }


    // ✅ 모든 액티비티 종료 후 마이페이지 이동
    /*private fun navigateToMyPage() {
        val intent = Intent(this@ReviewActivity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("FRAGMENT", "MyPageFragment") // ✅ 마이페이지 이동 플래그
        }
        startActivity(intent)
        finish() // 현재 액티비티 종료
    }*/

    override fun onResume() {
        super.onResume()
        loadPlaceFromPreferences() // ✅ 장소 데이터 불러오기
        loadBookFromPreferences()
    }

    // ✅ `SharedPreferences`에서 장소 데이터 불러오기
    private fun loadPlaceFromPreferences() {
        val sharedPref = getSharedPreferences("ReviewData", MODE_PRIVATE)
        val placeId = sharedPref.getLong("PLACE_ID", -1L)
        val placeTitle = sharedPref.getString("PLACE_TITLE", null)
        val placeImage = sharedPref.getString("PLACE_IMAGE", null)
        val placeRating = sharedPref.getFloat("PLACE_RATING", 0f)


        if (placeId != -1L && placeTitle != null && placeImage != null) {
            val newPlace = PlaceReviewItem(placeTitle, "위치", placeImage, placeRating)

            placeList.clear() // 기존 데이터 초기화
            placeList.add(newPlace)
            placeReviewAdapter.notifyDataSetChanged()
        }
    }

    private fun loadBookFromPreferences() {
        val sharedPref = getSharedPreferences("ReviewData", MODE_PRIVATE)
        val bookId = sharedPref.getLong("BOOK_ID", -1L)
        val bookTitle = sharedPref.getString("BOOK_TITLE", null)
        val bookCover = sharedPref.getString("BOOK_COVER", null)
        val bookRating = sharedPref.getFloat("BOOK_RATING", -1f)
        val bookAuthor = sharedPref.getString("BOOK_AUTHOR", null)

        if (bookId != -1L && bookTitle != null && bookCover != null && bookRating != -1f && bookAuthor != null) {
            val newBook = ReviewItem(bookTitle, bookAuthor, bookCover, bookRating)

            bookList.clear() // 기존 데이터 초기화
            bookList.add(newBook)
            reviewBookAdapter.notifyDataSetChanged()
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

    private fun clearReviewPreferences() {
        val sharedPref = getSharedPreferences("ReviewData", MODE_PRIVATE)
        sharedPref.edit().clear().apply() // ✅ SharedPreferences 데이터 삭제
    }

    override fun onDestroy() {
        super.onDestroy()
        clearReviewPreferences() // ✅ SharedPreferences 초기화
    }
}
