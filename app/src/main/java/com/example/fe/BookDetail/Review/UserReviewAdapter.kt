package com.example.fe.BookDetail.Review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.databinding.ItemUserReviewBinding

class UserReviewAdapter(private val reviewList: List<UserReview>) :
    RecyclerView.Adapter<UserReviewAdapter.UserReviewViewHolder>() {

    class UserReviewViewHolder(val binding: ItemUserReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserReviewViewHolder {
        val binding = ItemUserReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserReviewViewHolder, position: Int) {
        val review = reviewList[position]
        with(holder.binding) {
            userName.text = review.userName
            userReviewText.text = review.reviewText

            // ✅ Glide 사용하여 프로필 이미지 로드 (API 연결 고려)
            Glide.with(root.context)
                .load(review.profileImage) // 여기가 URL이라면, review.profileImage는 String이어야 함
                .into(userProfileImage)
        }
    }

    override fun getItemCount(): Int = reviewList.size
}
