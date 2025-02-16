package com.example.fe.scrap

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.databinding.FragmentScrapBottomSheetBinding
import com.example.fe.mypage.ScrapFolderResponse
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScrapBottomSheetFragment(
    private val bookId: Int?,
    private val placeId: Int?,
    private val onBookmarkStateChanged: (Boolean) -> Unit // 선택/해제 상태 콜백 추가
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentScrapBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScrapBottomSheetBinding.inflate(inflater, container, false)

        initScrapBottomSheetRV()
        initListeners()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setDimAmount(0.5f) // 배경을 50% 어둡게
    }

    private fun initScrapBottomSheetRV() {
        RetrofitClient.scrapApi.getScrapFolders().enqueue(object : Callback<ScrapFolderResponse> {
            override fun onResponse(
                call: Call<ScrapFolderResponse>,
                response: Response<ScrapFolderResponse>
            ) {
                if (response.isSuccessful) {
                    val scrapFolders = response.body()?.result?.folders ?: emptyList()
                    setupRecyclerView(scrapFolders)
                } else {
                    Log.e("ScrapAPI", "❌ API 요청 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ScrapFolderResponse>, t: Throwable) {
                Log.e("ScrapAPI", "❌ 네트워크 오류: ${t.message}")
            }
        })
    }

    // API에서 받아온 데이터를 리사이클러뷰에 설정
    private fun setupRecyclerView(scrapFolders: List<ScrapFolderResponse.Result.Folder>) {
        val adapter = ScrapBottomSheetRVAdapter(
            scrapFolders,
            bookId, // 전달된 도서 ID (null이면 공간 스크랩)
            placeId, // 전달된 공간 ID (null이면 도서 스크랩)
            { isSelected -> onBookmarkStateChanged(isSelected) },
            this
        )

        binding.scrapBottomSheetRv.adapter = adapter
        binding.scrapBottomSheetRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    // "새 스크랩" 클릭 시 다이얼로그 띄우기
    private fun initListeners() {
        binding.newScrapTv.setOnClickListener {
            val dialog = NewScrapDialogFragment(
                bookId = bookId,   // bookId 전달
                placeId = placeId, // placeId 전달
                onScrapCreated = {
                    onBookmarkStateChanged(true) // 북마크 상태 변경
                }
            )
            dismiss() // ScrapBottomSheetFragment 닫기
            dialog.show(parentFragmentManager, "NewScrapDialogFragment")
        }
    }
}