package com.example.fe.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fe.bookclub_book.server.api2
import com.example.fe.databinding.ActivityMypageScrapDetailBinding
import com.example.fe.mypage.adapter.MyPageScrapDetailRVAdapter
import com.example.fe.search.SearchMainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageScrapDetail : AppCompatActivity() {
    private lateinit var binding: ActivityMypageScrapDetailBinding

    private var isEditMode = false
    private val bookCursor = 0L
    private val placeCursor = 0L
    private val limit = 20
    private var folderId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageScrapDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.mypageScrapEditTv.setOnClickListener {
            toggleEditMode()
        }

        binding.addScrapBtn.setOnClickListener {
            val intent = Intent(this, SearchMainActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        initRecyclerView()
        initBookmarkClickListener()
        initListeners()

        folderId = intent.getIntExtra("folderId", -1)
        val folderName = intent.getStringExtra("folderName")
        if (folderId != -1) {
            fetchScraps(folderId)
            binding.folderNameTv.text = folderName ?: "Scrap Folder"
        } else {
            showError("Invalid folder ID")
        }
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(this, 3)
        binding.mypageScrapDetailRv.layoutManager = layoutManager

        val myPageScrapDetailRVAdapter =
            MyPageScrapDetailRVAdapter(object : MyPageScrapDetailRVAdapter.MyItemClickListener {
                override fun onItemClick(myPageScrap: Scrap) {
                    // 아이템 클릭 시 처리 로직
                }

                override fun onSelectionChanged(selectedCount: Int) {
                    // 선택 상태 변경 시 버튼 활성화 상태 업데이트
                    binding.scrapMoveBtn.isEnabled = selectedCount > 0
                    binding.scrapDeleteBtn.isEnabled = selectedCount > 0
                }
            })

        binding.mypageScrapDetailRv.adapter = myPageScrapDetailRVAdapter
    }

    private fun toggleEditMode() {
        isEditMode = !isEditMode

        // 편집 모드에 따라 버튼 보이기/숨기기
        // 편집 모드에 따라 버튼 보이기/숨기기
        if (isEditMode) {
            binding.bottomBtnsLl.visibility = View.VISIBLE
            binding.mypageScrapEditTv.text = "취소"
            binding.mypageScrapEditTv.setTextColor(android.graphics.Color.parseColor("#FF6363"))
        } else {
            binding.bottomBtnsLl.visibility = View.GONE
            binding.mypageScrapEditTv.text = "편집"
            binding.mypageScrapEditTv.setTextColor(android.graphics.Color.parseColor("#CFF305"))

            val adapter = binding.mypageScrapDetailRv.adapter as MyPageScrapDetailRVAdapter
            adapter.clearSelection()
        }

        binding.scrapMoveBtn.isEnabled = false
        binding.scrapDeleteBtn.isEnabled = false

        // 버튼 상태 업데이트
        binding.scrapMoveBtn.alpha = if (isEditMode) 1.0f else 0.5f
        binding.scrapDeleteBtn.alpha = if (isEditMode) 1.0f else 0.5f

        // 리사이클러뷰의 어댑터에 편집 모드 상태 전달
        (binding.mypageScrapDetailRv.adapter as MyPageScrapDetailRVAdapter).setEditMode(isEditMode)
    }

    private fun fetchScraps(folderId: Int) {
        api2.getScraps(folderId, bookCursor, placeCursor, limit).enqueue(object : Callback<MypageScrapsResponse> {
            override fun onResponse(
                call: Call<MypageScrapsResponse>,
                response: Response<MypageScrapsResponse>
            ) {
                if (response.isSuccessful) {
                    val scrapsResponse = response.body()
                    scrapsResponse?.let {
                        val adapter = binding.mypageScrapDetailRv.adapter as MyPageScrapDetailRVAdapter
                        adapter.setScraps(it.result.scraps)
                        binding.scrapCountTv.text = "${it.result.scraps.size}개의 스크랩"

                        if (it.result.scraps.isEmpty()) {
                            binding.mypageScrapDetailRv.visibility = View.GONE
                            binding.noScrapTv.visibility = View.VISIBLE
                            binding.addScrapBtn.visibility = View.VISIBLE
                        } else {
                            binding.mypageScrapDetailRv.visibility = View.VISIBLE
                            binding.noScrapTv.visibility = View.GONE
                            binding.addScrapBtn.visibility = View.GONE
                        }
                    }
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun initBookmarkClickListener() {
        binding.scrapMoveBtn.setOnClickListener {
            val scrapBottomSheet = MyPageScrapDetailBottomSheetFragment { newFolderId ->
                moveScrapsToNewFolder(newFolderId)
            }
            scrapBottomSheet.show(supportFragmentManager, scrapBottomSheet.tag)
        }
    }

    private fun initListeners() {
        // "스크랩 삭제" 클릭 시 다이얼로그 띄우기
        binding.scrapDeleteBtn.setOnClickListener {
            val adapter = binding.mypageScrapDetailRv.adapter as MyPageScrapDetailRVAdapter
            val selectedScraps = adapter.getSelectedScraps()
            val dialog = ScrapDeleteDialogFragment(folderId, selectedScraps) {
                fetchScraps(folderId) // 삭제 후 스크랩 목록 갱신
            }
            dialog.show(supportFragmentManager, "ScrapDeleteDialogFragment")
        }
    }

    private fun moveScrapsToNewFolder(newFolderId: Long) {
        val adapter = binding.mypageScrapDetailRv.adapter as MyPageScrapDetailRVAdapter
        val selectedScraps = adapter.getSelectedScraps().map { scrap ->
            MoveScrapsRequest.Scrap(scrapId = scrap.id, type = scrap.type)
        }

        val request = MoveScrapsRequest(newFolderId, selectedScraps)

        api2.moveScraps(folderId.toLong(), request).enqueue(object : Callback<MoveScrapsResponse> {
            override fun onResponse(call: Call<MoveScrapsResponse>, response: Response<MoveScrapsResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.isSuccess == true) {
                        Toast.makeText(this@MyPageScrapDetail, "스크랩이 이동되었습니다.", Toast.LENGTH_SHORT).show()
                        fetchScraps(folderId) // 이동 후 스크랩 목록 갱신
                    } else {
                        showError("Failed to move scraps: ${apiResponse?.message}")
                    }
                } else {
                    showError("API call failed with response code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MoveScrapsResponse>, t: Throwable) {
                showError(t.message ?: "API call failed")
            }
        })
    }
}