package com.example.fe.bookclub_book.adapter

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.R
import com.example.fe.bookclub_book.dataclass.CommentsResponse
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CommentsAdapter(
    private val context: Context,
    private val comments: List<CommentsResponse.Result.Comment>,
    private val onReplyClick: (Int) -> Unit // 답글 클릭 이벤트를 처리하기 위한 콜백
) : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("comments_prefs", Context.MODE_PRIVATE)

    class CommentViewHolder(
        itemView: View,
        private val onReplyClick: (Int) -> Unit, // onReplyClick을 생성자에서 받음
        private val comments: List<CommentsResponse.Result.Comment> // comments를 생성자에서 받음
    ) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val contentTv: TextView = itemView.findViewById(R.id.content_tv)
        val timeText: TextView = itemView.findViewById(R.id.time_text)
        val likeNum: TextView = itemView.findViewById(R.id.like_num)
        val userImage: ImageView = itemView.findViewById(R.id.user_image)
        val likeIcon: ImageView = itemView.findViewById(R.id.like_ic_iv)
        val replyButton: ImageView = itemView.findViewById(R.id.comment_ic_iv)

        init {
            replyButton.setOnClickListener {
                // 답글 버튼 클릭 시 해당 댓글 ID를 가져와 onReplyClick 호출
                val commentId = comments[adapterPosition].commentId // 댓글 ID 가져오기
                onReplyClick(commentId) // 답글 클릭 이벤트 처리
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bookclub_review_comment, parent, false)
        return CommentViewHolder(view, onReplyClick, comments) // 생성자에 전달
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.userName.text = comment.writerNickname
        holder.contentTv.text = comment.content

        // 시간 정보 로그 출력
        Log.d("CommentsAdapter", "Created At: ${comment.createdAt}")

        // ISO 8601 형식에 맞게 파싱 (나노초를 처리할 수 있는 패턴)
        val adjustedCreatedAt = "${comment.createdAt}Z" // UTC 시간대 가정
        val createdAt = ZonedDateTime.parse(adjustedCreatedAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            .withZoneSameInstant(ZoneId.systemDefault())

        holder.timeText.text = getTimeAgo(createdAt)
        holder.likeNum.text = "좋아요 ${comment.likeCount}"

        Glide.with(holder.userImage.context)
            .load(comment.writerProfileImg)
            .into(holder.userImage)

        // 좋아요 상태 복원
        comment.isLiked = sharedPreferences.getBoolean("comment_${comment.commentId}_isLiked", comment.isLiked)
        comment.likeCount = sharedPreferences.getInt("comment_${comment.commentId}_likeCount", comment.likeCount)

        // 좋아요 상태에 따른 색상 변경
        updateLikeStatus(holder, comment.isLiked)
        updateLikeText(holder, comment.likeCount)

        // 좋아요 버튼 클릭 리스너
        holder.likeNum.setOnClickListener {
            toggleLikeStatus(holder, comment)
        }

        holder.likeIcon.setOnClickListener {
            toggleLikeStatus(holder, comment)
        }
    }

    override fun getItemCount(): Int = comments.size

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTimeAgo(time: ZonedDateTime): String {
        // 현재 시간을 ZonedDateTime으로 가져오기
        val currentTime = ZonedDateTime.now(ZoneId.systemDefault())
        val duration = Duration.between(time, currentTime)
        val hours = duration.toHours()

        return if (hours > 0) {
            "$hours 시간 전"
        } else {
            "${duration.toMinutes()} 분 전"
        }
    }

    private fun updateLikeStatus(holder: CommentViewHolder, isLiked: Boolean) {
        if (isLiked) {
            holder.likeNum.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryNormal))
            holder.likeIcon.setImageResource(R.drawable.ic_like_on)
        } else {
            holder.likeNum.setTextColor(0xE0C2C4C8.toInt())
            holder.likeIcon.setImageResource(R.drawable.ic_like_off)
        }
    }

    private fun updateLikeText(holder: CommentViewHolder, likeCount: Int) {
        if (likeCount > 0) {
            holder.likeNum.text = "좋아요 $likeCount"
        } else {
            holder.likeNum.text = "좋아요"
        }
    }

    private fun toggleLikeStatus(holder: CommentViewHolder, comment: CommentsResponse.Result.Comment) {
        val newLikeStatus = !comment.isLiked
        val newLikeCount = if (newLikeStatus) comment.likeCount + 1 else comment.likeCount - 1

        // 로컬에 좋아요 상태 저장
        with(sharedPreferences.edit()) {
            putBoolean("comment_${comment.commentId}_isLiked", newLikeStatus)
            putInt("comment_${comment.commentId}_likeCount", newLikeCount)
            apply()
        }

        comment.isLiked = newLikeStatus
        comment.likeCount = newLikeCount

        updateLikeText(holder, comment.likeCount)
        updateLikeStatus(holder, comment.isLiked)
    }

    fun syncLikeStatusWithServer() {
        for (comment in comments) {
            // 서버로 전송 로직을 추가합니다.
        }
    }
}
