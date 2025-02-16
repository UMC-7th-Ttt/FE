package com.example.fe.scrap

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.example.fe.R
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.databinding.FragmentNewScrapDialogBinding
import com.example.fe.databinding.FragmentScrapCustomToastBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewScrapDialogFragment(
    private val bookId: Int?,
    private val placeId: Int?,
    private val onScrapCreated: () -> Unit // 북마크 상태 변경 콜백 추가
) : DialogFragment() {

    private lateinit var binding: FragmentNewScrapDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewScrapDialogBinding.inflate(inflater, container, false)
        initListeners()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setDimAmount(0.5f) // 배경 어둡게 처리

        dialog?.window?.apply {
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL) // 위쪽 정렬

            attributes = attributes.apply {
                y = 500 // 중앙 정렬
            }

            setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명
        }

        Handler(Looper.getMainLooper()).postDelayed({
            showKeyboard()
        }, 200) // 200ms 후 키보드 실행
    }

    private fun showKeyboard() {
        binding.scrapNameEt.requestFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.scrapNameEt, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun initListeners() {
        // 글자 수 업데이트
        binding.scrapNameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val currentLength = s?.length ?: 0
                binding.charCountTv.text = "$currentLength/10"
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // EditText 키보드 "완료(✔)" 버튼 클릭 시
        binding.scrapNameEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleScrapCreation()
                true
            } else {
                false
            }
        }

        // "완료" 버튼 클릭 시
        binding.newScrapCompeleteBtn.setOnClickListener {
            handleScrapCreation()
        }

        // "취소" 버튼 클릭 시
        binding.newScrapCancleBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun handleScrapCreation() {
        val scrapName = binding.scrapNameEt.text.toString().trim()

        if (scrapName.isNotEmpty()) {
            sendScrapRequest(scrapName) // API 연동
        } else {
            Toast.makeText(requireContext(), "스크랩명을 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    // 스크랩 API 요청
    private fun sendScrapRequest(folderName: String) {
        if (bookId != null) {
            // 도서 스크랩
            RetrofitClient.scrapApi.scrapBook(bookId, folderName).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        showCustomToast(folderName)
                        onScrapCreated()
                        dismiss()
                    } else {
                        Toast.makeText(requireContext(), "스크랩 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(requireContext(), "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else if (placeId != null) {
            // 공간 스크랩
            RetrofitClient.scrapApi.scrapPlace(placeId, folderName).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        showCustomToast(folderName)
                        onScrapCreated()
                        dismiss()
                    } else {
                        Toast.makeText(requireContext(), "스크랩 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(requireContext(), "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    // 스크랩 완료 후 커스텀 토스트 표시
    private fun showCustomToast(scrapName: String) {
        val inflater = LayoutInflater.from(requireContext())
        val toastBinding = FragmentScrapCustomToastBinding.inflate(inflater)

        toastBinding.scrapItemIv.setImageResource(R.drawable.img_scrap_user_add)
        toastBinding.scrapItemNameTv.text = "$scrapName 에 저장됨"

        val toast = Toast(requireContext()).apply {
            duration = Toast.LENGTH_SHORT
            setView(toastBinding.root)
            setGravity(Gravity.TOP, 0, 200)
        }

        toast.show()
    }
}