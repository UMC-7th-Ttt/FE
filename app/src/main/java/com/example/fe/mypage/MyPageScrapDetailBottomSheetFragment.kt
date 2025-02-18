package com.example.fe.mypage

import com.example.fe.scrap.ScrapBottomSheetRVAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R
import com.example.fe.databinding.FragmentMypageScrapDetailBottomSheetBinding
import com.example.fe.databinding.FragmentScrapBottomSheetBinding
import com.example.fe.mypage.adapter.MyPageScrapDetailBottomSheetRVAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyPageScrapDetailBottomSheetFragment(
    private val onFolderSelected: (newFolderId: Long) -> Unit
) : BottomSheetDialogFragment() {


    private lateinit var binding: FragmentMypageScrapDetailBottomSheetBinding
    private lateinit var myPageScrapDetailBottomSheetRVAdapter: MyPageScrapDetailBottomSheetRVAdapter

    private var selectedFolderId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMypageScrapDetailBottomSheetBinding.inflate(inflater, container, false)

        initScrapBottomSheetRV()
        initClickListeners()
        fetchFolders()

        return binding.root
    }

    private fun initScrapBottomSheetRV() {
        myPageScrapDetailBottomSheetRVAdapter = MyPageScrapDetailBottomSheetRVAdapter(emptyList()) { folderId ->
            selectedFolderId = folderId
            binding.moveBtn.isEnabled = true
        }
        binding.scrapBottomSheetRv.adapter = myPageScrapDetailBottomSheetRVAdapter
        binding.scrapBottomSheetRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    private fun initClickListeners() {
        binding.moveBtn.setOnClickListener {
            selectedFolderId?.let { folderId ->
                onFolderSelected(folderId)
                dismiss()
            }
        }

        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
    }


    private fun fetchFolders() {
        api.getFolders().enqueue(object : Callback<ScrapFolderResponse> {
            override fun onResponse(call: Call<ScrapFolderResponse>, response: Response<ScrapFolderResponse>) {
                if (response.isSuccessful) {
                    val scrapFolderResponse = response.body()
                    scrapFolderResponse?.let {
                        myPageScrapDetailBottomSheetRVAdapter.updateFolders(it.result.folders)
                    }
                } else {
                    Log.e("MyPageScrapFragment", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ScrapFolderResponse>, t: Throwable) {
                Log.e("MyPageScrapFragment", "Network Error: ${t.message}")
            }
        })
    }

}
