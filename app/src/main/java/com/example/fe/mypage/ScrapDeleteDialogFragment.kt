package com.example.fe.mypage

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import android.view.ViewGroup.LayoutParams
import com.example.fe.databinding.FragmentDeleteScrapDialogBinding
import com.example.fe.databinding.FragmentScrapCustomToastBinding
import com.example.fe.bookclub_book.server.api2
import com.example.fe.databinding.FragmentScrapDeleteCustomToastBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScrapDeleteDialogFragment(
    private val folderId: Int,
    private val selectedScraps: List<Scrap>,
    private val onScrapsDeleted: () -> Unit
) : DialogFragment() {

    private lateinit var binding: FragmentDeleteScrapDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeleteScrapDialogBinding.inflate(inflater, container, false)
        initListeners()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            LayoutParams.WRAP_CONTENT, // 다이얼로그의 가로 크기
            LayoutParams.WRAP_CONTENT // 다이얼로그의 세로 크기
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명 처리
    }

    private fun initListeners() {
        // "삭제" 버튼 클릭 시
        binding.deleteBtn.setOnClickListener {
            deleteScraps()
        }

        // "취소" 버튼 클릭 시
        binding.cancelBtn.setOnClickListener {
            dismiss() // 다이얼로그 닫기
        }
    }

    private fun deleteScraps() {
        val requestBody = ScrapDeleteRequest(
            scraps = selectedScraps.map { scrap ->
                ScrapDeleteItem(scrapId = scrap.id, type = scrap.type)
            }
        )

        api2.deleteScraps(folderId, requestBody).enqueue(object : Callback<MypageScrapsResponse> {
            override fun onResponse(call: Call<MypageScrapsResponse>, response: Response<MypageScrapsResponse>) {
                if (response.isSuccessful) {
                    showCustomToast()
                    onScrapsDeleted()
                    dismiss()
                } else {
                    showError("API call failed with response code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MypageScrapsResponse>, t: Throwable) {
                showError(t.message ?: "API call failed")
            }
        })
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showCustomToast() {
        val inflater = LayoutInflater.from(requireContext())
        val toastBinding = FragmentScrapDeleteCustomToastBinding.inflate(inflater)

        // 토스트 객체 생성
        val toast = Toast(requireContext()).apply {
            duration = Toast.LENGTH_SHORT
            view = toastBinding.root
            setGravity(android.view.Gravity.TOP, 0, 200) // 위치 조정: 상단에 표시
        }

        toast.show()
    }
}