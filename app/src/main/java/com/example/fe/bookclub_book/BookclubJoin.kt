package com.example.fe.bookclub_book

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.MainActivity
import com.example.fe.R
import com.example.fe.bookclub_book.adapter.BookInfoTagRVAdapter
import com.example.fe.bookclub_book.dataclass.BookClubBookJoin
import com.example.fe.bookclub_book.dataclass.BookClubJoinInfoResponse
import com.example.fe.bookclub_book.dataclass.BookClubJoinResponse
import com.example.fe.bookclub_book.server.api
import com.example.fe.databinding.ActivityBookclubJoinBinding
import com.example.fe.databinding.FragmentScrapCustomToastBinding
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

        // Intent로부터 bookClubId를 받아옴
        val bookClubId = intent.getIntExtra("bookClubId", -1)
        fetchBookClubInfo(bookClubId)

        initTagRV()
    }

    private fun fetchBookClubInfo(bookClubId: Int) {
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
                            joinBookClub(bookClubId.toLong())
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

    private fun joinBookClub(bookClubId: Long) {
        api.joinBookClub(bookClubId).enqueue(object : Callback<BookClubJoinResponse> {
            override fun onResponse(call: Call<BookClubJoinResponse>, response: Response<BookClubJoinResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.isSuccess) {
                            showCustomToast("가입이 완료되었습니다!")
                        } else {
                            Toast.makeText(this@BookclubJoin, "가입 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // 오류 처리
                    println("Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<BookClubJoinResponse>, t: Throwable) {
                println("Network Error: ${t.message}")
            }
        })
    }

    private fun initTagRV() {
        val bookInfoTagRecyclerView = findViewById<RecyclerView>(R.id.book_info_tag_fb)
        bookInfoTagRecyclerView.layoutManager = FlexboxLayoutManager(this)

        bookInfoTagList = listOf()

        bookInfoTagRVAdapter = BookInfoTagRVAdapter(bookInfoTagList)
        bookInfoTagRecyclerView.adapter = bookInfoTagRVAdapter
    }

    private fun updateTagList(author: String, page: String, publisher: String, hasEbook: Boolean, category: String) {
        val tags = mutableListOf(author, page, publisher, category)

        bookInfoTagList = tags
        bookInfoTagRVAdapter.updateTags(bookInfoTagList)

        if (hasEbook) {
            tags.add("E북 등록")
        }
    }

    private fun showCustomToast(bookCoverUrl: String) {
        val inflater = LayoutInflater.from(this)
        val toastBinding = FragmentScrapCustomToastBinding.inflate(inflater)

        // Glide를 사용하여 이미지 로드
        Glide.with(this)
            .load(bookCoverUrl) // 책 커버 이미지 URL
            .into(toastBinding.scrapItemIv) // 아이콘 설정

        toastBinding.scrapItemNameTv.text = "가입이 완료되었습니다!"

        // 토스트 객체 생성
        val toast = Toast(this).apply {
            duration = Toast.LENGTH_SHORT
            view = toastBinding.root
            setGravity(android.view.Gravity.TOP, 0, 200) // 위치 조정: 상단에 표시
        }

        toast.show()
    }
}