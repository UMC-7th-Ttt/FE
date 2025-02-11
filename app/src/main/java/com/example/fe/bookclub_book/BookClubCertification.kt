package com.example.fe.bookclub_book

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.R
import com.example.fe.bookclub_book.adapter.CertifyPhotoRVAdapter
import com.example.fe.bookclub_book.server.BookClubCertificationRequest
import com.example.fe.bookclub_book.server.BookClubCertificationResponse
import com.example.fe.bookclub_book.server.api
import com.example.fe.databinding.ActivityBookclubCertificationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookClubCertification : AppCompatActivity() {

    private lateinit var binding: ActivityBookclubCertificationBinding
    private lateinit var imageAdapter: CertifyPhotoRVAdapter
    private val selectedImages = mutableListOf<Uri>()
    private lateinit var titleEditText: EditText
    private lateinit var reviewEditText: EditText
    private lateinit var pageCountEditText: EditText
    private var bookClubId: Int = -1

    @SuppressLint("IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookclubCertificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        // RecyclerView 초기화
        imageAdapter = CertifyPhotoRVAdapter(selectedImages)
        binding.photoRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.photoRv.adapter = imageAdapter

        // 이미지 선택 버튼 클릭 리스너
        binding.photoIv1.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        titleEditText = findViewById(R.id.title_edit_text)
        reviewEditText = findViewById(R.id.review_edit_text)
        pageCountEditText = findViewById(R.id.page_count_edit_text)

        // EditText 리스트 생성
        val editTexts = listOf(titleEditText, reviewEditText, pageCountEditText)

        fun checkEditTexts() {
            val allFilled = editTexts.all { it.text.isNotEmpty() }
            binding.doneBtn.isEnabled = allFilled // 버튼 활성화/비활성화
        }

        // EditText에 TextWatcher 추가
        editTexts.forEach { editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    checkEditTexts() // 텍스트가 변경될 때마다 호출
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }

        // EditText 배경 설정 함수
        fun setEditTextBackground(editText: EditText, isFocused: Boolean) {
            if (isFocused) {
                editText.setBackgroundResource(R.drawable.primary_border_radius_8)
            } else {
                editText.setBackgroundResource(R.drawable.edit_text_radius_8)
            }
        }

        // EditText 높이 조절 함수
        fun adjustEditTextHeight(editText: EditText) {
            val lineHeight = editText.lineHeight // 각 줄의 높이
            val lineCount = editText.lineCount // 현재 줄 수
            val padding = editText.paddingTop + editText.paddingBottom // 패딩

            // EditText의 높이를 설정
            editText.layoutParams.height = lineHeight * lineCount + padding
            editText.requestLayout() // 레이아웃 갱신
        }


        titleEditText.setOnClickListener {
            setEditTextBackground(titleEditText, true)
        }

        titleEditText.setOnFocusChangeListener { _, hasFocus ->
            setEditTextBackground(titleEditText, hasFocus)
        }

        titleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setEditTextBackground(titleEditText, true)
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        // Review EditText 설정
        reviewEditText.setOnClickListener {
            setEditTextBackground(reviewEditText, true)
        }

        reviewEditText.setOnFocusChangeListener { _, hasFocus ->
            setEditTextBackground(reviewEditText, hasFocus)
        }

        reviewEditText.addTextChangedListener(object : TextWatcher {
            var isEditing = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 현재 입력된 텍스트의 길이를 가져옴
                val currentLength = s?.length ?: 0

                // 글자 수 제한
                if (currentLength > 300) {
                    if (!isEditing) { // 편집 중이 아닐 때만 실행
                        isEditing = true // 편집 중으로 설정
                        if (s != null) {
                            reviewEditText.setText(s.subSequence(0, 300))
                        } // 최대 300자까지만 유지
                        reviewEditText.setSelection(300) // 커서 위치 조정
                        isEditing = false // 편집 종료
                        return // 이 시점에서 함수를 종료하여 카운트가 더해지지 않도록 함
                    }
                }

                // Review Count TextView 업데이트
                binding.reviewCountTv.text =  "$currentLength/300"

                // EditText 높이 조절
                adjustEditTextHeight(reviewEditText)
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        // Page Count EditText 설정
        pageCountEditText.setOnClickListener {
            setEditTextBackground(pageCountEditText, true)
        }

        pageCountEditText.setOnFocusChangeListener { _, hasFocus ->
            setEditTextBackground(pageCountEditText, hasFocus)
        }

        pageCountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setEditTextBackground(pageCountEditText, true)

                // 사용자가 입력할 수 있는 숫자만 허용
                if (!s.isNullOrEmpty() && !s.toString().matches("\\d*".toRegex())) {
                    // 마지막 입력이 숫자가 아닐 경우 입력 내용 지우기
                    pageCountEditText.setText(s.subSequence(0, s.length - 1)) // 마지막 문자 지우기
                    pageCountEditText.setSelection(pageCountEditText.text.length) // 커서 위치 조정
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 인증 버튼 클릭 리스너
        binding.doneBtn.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = reviewEditText.text.toString()
            val currentPage = pageCountEditText.text.toString().toInt()
            val imgUrl = if (selectedImages.isNotEmpty()) selectedImages[0].toString() else ""
            val isSecret = false

            val request = BookClubCertificationRequest(
                title = title,
                content = content,
                currentPage = currentPage,
                imgUrl = imgUrl,
                isSecret = isSecret
            )

            sendCertification(request)
        }

        bookClubId = intent.getIntExtra("bookClubId", -1)
    }

    private fun sendCertification(request: BookClubCertificationRequest) {
        Log.d("BookClubCertification", "bookClubId: $bookClubId") // 로그 추가
        if (bookClubId == -1) {
            Toast.makeText(this, "Invalid book club ID", Toast.LENGTH_SHORT).show()
            return
        }

        api.postCertification(bookClubId, request).enqueue(object :
            Callback<BookClubCertificationResponse> {
            override fun onResponse(call: Call<BookClubCertificationResponse>, response: Response<BookClubCertificationResponse>) {
                if (response.isSuccessful) {
                    val certificationResponse = response.body()
                    certificationResponse?.let {
                        if (it.isSuccess) {
                            Toast.makeText(this@BookClubCertification, "Certification successful!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@BookClubCertification, "Failed to certify: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@BookClubCertification, "Failed to certify: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BookClubCertificationResponse>, t: Throwable) {
                Toast.makeText(this@BookClubCertification, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedImages.add(uri) // 선택한 이미지 URI 추가
                imageAdapter.notifyDataSetChanged() // RecyclerView 업데이트
            }
        }
    }
}