package com.example.fe.bookclub_book

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.fe.bookclub_book.adapter.CommentsAdapter
import com.example.fe.bookclub_book.dataclass.BookClubReviewDetailResponse
import com.example.fe.bookclub_book.dataclass.CommentsResponse
import com.example.fe.bookclub_book.dataclass.CommentRequest
import com.example.fe.bookclub_book.server.api
import com.example.fe.databinding.ActivityBookclubBookReviewDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookclubBookReviewDetail : AppCompatActivity() {

    private lateinit var binding: ActivityBookclubBookReviewDetailBinding
    private lateinit var commentsAdapter: CommentsAdapter
    private var comments = mutableListOf<CommentsResponse.Result.Comment>()
    private var readingRecordId: Int = -1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookclubBookReviewDetailBinding.inflate(layoutInflater)

        binding.backBtn.setOnClickListener {
            finish()
        }

        setContentView(binding.root)

        commentsAdapter = CommentsAdapter(this, comments) { commentId ->
            showReplyInput(commentId) // 답글 입력창 표시
        }

        binding.commentsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.commentsRecyclerView.adapter = commentsAdapter

        val readingRecordId = intent.getIntExtra("readingRecordId", -1)
        if (readingRecordId != -1) {
            fetchBookClubReviewDetail(readingRecordId)
            fetchComments(readingRecordId)
        }

        binding.floatingCommentIcon.setOnClickListener {
            postComment(readingRecordId)
        }
    }

    private fun fetchBookClubReviewDetail(readingRecordId: Int) {
        api.getBookClubReviewDetail(readingRecordId).enqueue(object : Callback<BookClubReviewDetailResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<BookClubReviewDetailResponse>, response: Response<BookClubReviewDetailResponse>) {
                if (response.isSuccessful) {
                    val bookClubReviewDetail = response.body()
                    bookClubReviewDetail?.let {
                        binding.nicknameTv.text = it.result.memberInfo.nickname
                        binding.reviewTitleTv.text = it.result.title
                        binding.reviewMainTv.text = it.result.content

                        // currentPage를 세로로 변환하여 설정
                        binding.currentPageTv.text = it.result.currentPage.toString().toCharArray().joinToString("\n")

                        Glide.with(this@BookclubBookReviewDetail)
                            .load(it.result.memberInfo.profileUrl)
                            .into(binding.profileIv)

                        // 리뷰 이미지 로드를 위한 코드
                        Glide.with(this@BookclubBookReviewDetail)
                            .load(it.result.imgUrl)
                            .into(binding.reviewPhotoIv)
                    }
                } else {
                    // 오류 처리
                    Log.e("BookclubBookReviewDetail", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<BookClubReviewDetailResponse>, t: Throwable) {
                Log.e("BookclubBookReviewDetail", "Network Error: ${t.message}")
            }
        })
    }

    private fun fetchComments(readingRecordId: Int) {
        api.getComments(readingRecordId).enqueue(object : Callback<CommentsResponse> {
            @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
            override fun onResponse(call: Call<CommentsResponse>, response: Response<CommentsResponse>) {
                if (response.isSuccessful) {
                    val commentsResponse = response.body()
                    commentsResponse?.let {
                        binding.commentCountTv.text = "댓글 ${it.result.commentCount}"

                        if (it.result.commentCount > 0) {
                            binding.commentsRecyclerView.visibility = View.VISIBLE
                            comments.clear()
                            comments.addAll(it.result.comments)
                            commentsAdapter.notifyDataSetChanged()
                        } else {
                            binding.commentsRecyclerView.visibility = View.GONE
                        }
                    }
                } else {
                    Log.e("BookclubBookReviewDetail", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CommentsResponse>, t: Throwable) {
                Log.e("BookclubBookReviewDetail", "Network Error: ${t.message}")
            }
        })
    }

    private fun showReplyInput(commentId: Int) {
        binding.floatingCommentEditText.visibility = View.VISIBLE
        binding.floatingCommentEditText.setHint("답글을 입력하세요") // 힌트 설정
        binding.floatingCommentEditText.tag = commentId // 댓글 ID를 태그로 설정
        binding.floatingCommentIcon.setOnClickListener {
            postReply(readingRecordId, commentId) // 답글 작성
        }
    }

    private fun postComment(readingRecordId: Int) {
        val content = binding.floatingCommentEditText.text.toString().trim()
        if (content.isEmpty()) {
            Toast.makeText(this, "댓글을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        val commentRequest = CommentRequest(content)

        api.postComment(readingRecordId, commentRequest).enqueue(object : Callback<CommentsResponse> {
            override fun onResponse(call: Call<CommentsResponse>, response: Response<CommentsResponse>) {
                if (response.isSuccessful) {
                    Log.d("BookclubBookReviewDetail", "Response: ${response.body()}")
                    binding.floatingCommentEditText.text.clear()
                    hideKeyboard()
                    fetchComments(readingRecordId) // 댓글 작성 후 댓글 목록을 다시 가져옵니다.
                } else {
                    Log.e("BookclubBookReviewDetail", "Error: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@BookclubBookReviewDetail, "댓글 작성에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CommentsResponse>, t: Throwable) {
                Log.e("BookclubBookReviewDetail", "Network Error: ${t.message}")
                Toast.makeText(this@BookclubBookReviewDetail, "네트워크 오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun postReply(readingRecordId: Int, parentCommentId: Int) {
        val content = binding.floatingCommentEditText.text.toString().trim()
        if (content.isEmpty()) {
            Toast.makeText(this, "답글을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        val commentRequest = CommentRequest(content, parentCommentId)

        // 디버깅을 위한 로그 출력
        Log.d("BookclubBookReviewDetail", "Posting reply with content: $content and parentCommentId: $parentCommentId")

        api.postComment(readingRecordId, commentRequest).enqueue(object : Callback<CommentsResponse> {
            override fun onResponse(call: Call<CommentsResponse>, response: Response<CommentsResponse>) {
                if (response.isSuccessful) {
                    Log.d("BookclubBookReviewDetail", "Response: ${response.body()}")
                    binding.floatingCommentEditText.text.clear()
                    hideKeyboard()
                    fetchComments(readingRecordId) // 댓글 목록을 다시 가져옵니다.
                } else {
                    Log.e("BookclubBookReviewDetail", "Error: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@BookclubBookReviewDetail, "답글 작성에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CommentsResponse>, t: Throwable) {
                Log.e("BookclubBookReviewDetail", "Network Error: ${t.message}")
                Toast.makeText(this@BookclubBookReviewDetail, "네트워크 오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }




    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.floatingCommentEditText.windowToken, 0)
    }

    override fun onPause() {
        super.onPause()
        commentsAdapter.syncLikeStatusWithServer()
    }
}