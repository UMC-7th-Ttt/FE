package com.example.fe.bookclub_book

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.HomeFragment
import com.example.fe.MainActivity
import com.example.fe.R
import com.example.fe.bookclub_book.adapter.BookInfoTagRVAdapter
import com.example.fe.bookclub_book.dataclass.BookClubBookJoin
import com.example.fe.bookclub_book.server.BookClubJoinInfoResponse
import com.example.fe.bookclub_book.server.api
import com.example.fe.databinding.ActivityBookclubJoinBinding
import com.google.android.flexbox.FlexboxLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookclubJoin : AppCompatActivity() {

    private lateinit var binding: ActivityBookclubJoinBinding

    private lateinit var bookInfoTagRVAdapter: BookInfoTagRVAdapter
    private lateinit var bookInfoTagList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookclubJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }
        
        // 홈 아이콘 클릭 리스너
        binding.homeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("load_home_fragment", true)
            startActivity(intent)
            // 현재 Activity 종료
            finish()
        }

        val id = 2
        val bookClubId = 1
        fetchBookClubInfo(bookClubId)

        initTagRV()

//        binding.joinBtn.setOnClickListener {
//            val bookClubBookJoin = BookClubBookJoin(id = id, bookClubId = bookClubId)
//            postBookClubJoinInfo(bookClubBookJoin)
//        }
    }

    fun fetchBookClubInfo(bookClubId: Int) {
        api.getBookClubInfo(bookClubId).enqueue(object : Callback<BookClubJoinInfoResponse> {
            override fun onResponse(call: Call<BookClubJoinInfoResponse>, response: Response<BookClubJoinInfoResponse>) {
                if (response.isSuccessful) {
                    val bookclubInfo = response.body()
                    bookclubInfo?.let {
                        binding.bookTitleTv.text = it.result.bookInfo.title
                        binding.bookAuthorTv.text = it.result.bookInfo.author
                        binding.publisherTv.text = it.result.bookInfo.publisher
                        binding.publisherReviewDetailTv.text = it.result.bookInfo.description

                        binding.startDateTv.text = it.result.startDate
                        binding.endDateTv.text = it.result.endDate
                        binding.recruitNumberTv2.text = it.result.recuitNumber.toString()

                        updateTagList(
                            it.result.bookInfo.author,
                            "${it.result.bookInfo.itemPage}쪽",
                            it.result.bookInfo.publisher,
                            it.result.bookInfo.hasEbook,
                            it.result.bookInfo.category
                        )

                        Glide.with(this@BookclubJoin)
                            .load(it.result.bookInfo.cover)
                            .into(binding.bookIv)

                        Glide.with(this@BookclubJoin)
                            .load(it.result.bookInfo.cover)
                            .into(binding.bookBgIv)

                        binding.joinBtn.setOnClickListener { view ->
                            showJoinCompleteToast(it.result.bookInfo.cover)
                        }

                    }
                } else {
                    // 오류 처리
                    println("Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<BookClubJoinInfoResponse>, t: Throwable) {
                println("Network Error: ${t.message}")
            }
        })
    }

    private fun postBookClubJoinInfo(bookClubBookJoin: BookClubBookJoin) {
        api.joinBookClub(bookClubBookJoin).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@BookclubJoin, "가입 완료!", Toast.LENGTH_SHORT).show()
                } else {
                    // 오류 처리
                    println("Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Network Error: ${t.message}")
            }
        })
    }

    fun initTagRV() {
        val bookInfoTagRecyclerView = findViewById<RecyclerView>(R.id.book_info_tag_fb)
        bookInfoTagRecyclerView.layoutManager = FlexboxLayoutManager(this)

        bookInfoTagList = listOf()

        bookInfoTagRVAdapter = BookInfoTagRVAdapter(bookInfoTagList)
        bookInfoTagRecyclerView.adapter = bookInfoTagRVAdapter
    }

    private fun updateTagList(author: String, page:String, publisher: String, hasEbook: Boolean, category: String) {
        val tags = mutableListOf(author, page, publisher, category)

        bookInfoTagList = tags
        bookInfoTagRVAdapter.updateTags(bookInfoTagList)

        if (hasEbook) {
            tags.add("E북 등록")
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showJoinCompleteToast(bookCoverUrl: String) {
        // 커스텀 레이아웃 인플레이트
        val toastLayout = layoutInflater.inflate(R.layout.bookclub_join_toast, null)

        // 책 커버 이미지를 Glide로 로드
        val bookCoverImageView = toastLayout.findViewById<ImageView>(R.id.toast_book_cover_iv)
        Glide.with(this)
            .load(bookCoverUrl) // 책 커버 이미지 URL
            .into(bookCoverImageView)

        // 토스트 생성
        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = toastLayout

        // 토스트 위치 설정 (화면 상단)
        toast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 290)
        toast.show()
    }
}