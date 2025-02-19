package com.example.fe.Review

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.MainActivity
import com.example.fe.R
import com.example.fe.databinding.ActivityReviewBinding
import com.example.fe.mypage.MyPageFragment
import com.example.fe.search.SearchMainActivity
import com.example.fe.search.SearchResultFragment

class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding
    private lateinit var reviewBookAdapter: ReviewBookAdapter
    private val bookList = mutableListOf<ReviewItem>() // ✅ 리스트 추가 (메모리 관리)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기 버튼 비활성화
        binding.submitButton.isEnabled = false

        // ✅ RecyclerView 설정
        reviewBookAdapter = ReviewBookAdapter(bookList)
        binding.locationRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ReviewActivity)
            adapter = reviewBookAdapter
        }

        // ✅ 완료 버튼 클릭 시 MyPageFragment로 이동
        binding.submitButton.setOnClickListener {
            Toast.makeText(this, "서평이 저장되었습니다!", Toast.LENGTH_SHORT).show()
            clearSearchResultFragment() // ✅ `SearchResultFragment`만 제거

            val myPageFragment = MyPageFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_layout, myPageFragment)
                .commit()

            finish() // ✅ `ReviewActivity` 종료
        }

        // ✅ 도서 / 장소 추가 버튼 클릭 → `SearchMainActivity`로 이동
        binding.addLocationButton.setOnClickListener {
            val intent = Intent(this, SearchMainActivity::class.java).apply {
                putExtra("CALLER", "ReviewActivity")
            }
            startActivity(intent)
        }

        // ✅ 뒤로 가기 버튼 클릭 (초기화 후 `MyPageFragment`로 이동)
        binding.backButton.setOnClickListener {
            clearAllData() // ✅ 데이터 초기화
            clearSearchResultFragment() // ✅ `SearchResultFragment`만 제거

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("SHOW_FRAGMENT", "MyPageFragment") // ✅ MyPageFragment를 표시하기 위한 데이터 전달
            }
            startActivity(intent)

            finish() // ✅ `ReviewActivity` 종료
        }


        // ✅ 제목 및 서평 입력 감지하여 버튼 활성화
        binding.reviewInput.addTextChangedListener(textWatcher)
        binding.titleInput.addTextChangedListener(textWatcher)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent) // ✅ 새로운 인텐트 저장
    }

    override fun onResume() {
        super.onResume()
    }

    // ✅ 입력값 및 RecyclerView 데이터 초기화
    private fun clearAllData() {
        binding.titleInput.text.clear()
        binding.reviewInput.text.clear()

        // ✅ RecyclerView 데이터 초기화
        bookList.clear()
        reviewBookAdapter.notifyDataSetChanged()
    }

    // ✅ `SearchResultFragment`만 제거 (나머지는 유지)
    private fun clearSearchResultFragment() {
        val fragmentManager = supportFragmentManager
        val searchResultFragment = fragmentManager.findFragmentByTag("SearchResultFragment")

        searchResultFragment?.let {
            fragmentManager.beginTransaction()
                .remove(it)
                .commitAllowingStateLoss()
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
