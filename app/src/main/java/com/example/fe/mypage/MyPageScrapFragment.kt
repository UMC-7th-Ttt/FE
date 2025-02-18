package com.example.fe.mypage

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fe.bookclub_book.server.api2
import com.example.fe.databinding.FragmentMypageScrapBinding
import com.example.fe.mypage.adapter.MyPageScrapRVAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageScrapFragment : Fragment() {
    lateinit var binding: FragmentMypageScrapBinding
    private lateinit var myPageScrapRVAdapter: MyPageScrapRVAdapter
    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageScrapBinding.inflate(inflater, container, false)

        initScrapRecyclerview()
        fetchFolders()

        binding.mypageScrapEditTv.setOnClickListener {
            toggleEditMode()
        }

        binding.folderDeleteBtn.setOnClickListener {
            deleteSelectedFolders()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        fetchFolders() // 액티비티가 다시 활성화될 때 폴더 목록을 업데이트
    }

    private fun toggleEditMode() {
        isEditMode = !isEditMode

        // 편집 모드에 따라 버튼 보이기/숨기기
        if (isEditMode) {
            binding.folderDeleteBtn.visibility = View.VISIBLE
            binding.mypageScrapEditTv.text = "취소"
            binding.mypageScrapEditTv.setTextColor(Color.parseColor("#FF6363"))
        } else {
            binding.folderDeleteBtn.visibility = View.GONE
            binding.mypageScrapEditTv.text = "편집"
            binding.mypageScrapEditTv.setTextColor(Color.parseColor("#CFF305"))

            myPageScrapRVAdapter.clearSelection()
        }

        // 버튼 상태 업데이트
        binding.folderDeleteBtn.isEnabled = false
        binding.folderDeleteBtn.alpha = if (isEditMode) 1.0f else 0.5f

        // 리사이클러뷰의 어댑터에 편집 모드 상태 전달
        myPageScrapRVAdapter.setEditMode(isEditMode)
    }

    private fun initScrapRecyclerview() {
        binding.scrapRv.layoutManager = GridLayoutManager(context, 2)

        myPageScrapRVAdapter = MyPageScrapRVAdapter(object : MyPageScrapRVAdapter.MyItemClickListener {
            override fun onItemClick(folderId: Int) {
                if (!isEditMode) {
                    val intent = Intent(context, MyPageScrapDetail::class.java)
                    intent.putExtra("folderId", folderId)
                    startActivity(intent)
                }
            }

            override fun onSelectionChanged(selectedCount: Int) {
                // 선택 상태 변경 시 버튼 활성화 상태 업데이트
                binding.folderDeleteBtn.isEnabled = selectedCount > 0
            }
        })

        binding.scrapRv.adapter = myPageScrapRVAdapter
    }

    private fun fetchFolders() {
        api.getFolders().enqueue(object : Callback<ScrapFolderResponse> {
            override fun onResponse(call: Call<ScrapFolderResponse>, response: Response<ScrapFolderResponse>) {
                if (response.isSuccessful) {
                    val scrapFolderResponse = response.body()
                    scrapFolderResponse?.let {
                        myPageScrapRVAdapter.setScrap(it.result.folders)
                        binding.scrapCountTv.text = it.result.folderCount.toString()
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

    private fun deleteSelectedFolders() {
        val selectedFolders = myPageScrapRVAdapter.getSelectedItems()
        selectedFolders.forEach { folder ->
            api2.deleteFolder(folder.folderId).enqueue(object : Callback<DeleteFolderResponse> {
                override fun onResponse(call: Call<DeleteFolderResponse>, response: Response<DeleteFolderResponse>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse?.isSuccess == true) {
                            fetchFolders() // 삭제 후 폴더 목록 갱신
                        } else {
                            Log.e("MyPageScrapFragment", "Failed to delete folder: ${apiResponse?.message}")
                        }
                    } else {
                        Log.e("MyPageScrapFragment", "API call failed with response code ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<DeleteFolderResponse>, t: Throwable) {
                    Log.e("MyPageScrapFragment", "API call failed: ${t.message}")
                }
            })
        }
    }
}