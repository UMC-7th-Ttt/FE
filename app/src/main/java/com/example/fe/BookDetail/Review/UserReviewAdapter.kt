package com.example.fe.BookDetail.Review

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R

class UserReviewAdapter(private val reviewList: List<UserReview>) :
    RecyclerView.Adapter<UserReviewAdapter.UserReviewViewHolder>() {

    class UserReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileImage: ImageView = view.findViewById(R.id.user_profile_image)
        val userName: TextView = view.findViewById(R.id.user_name)
        val reviewText: TextView = view.findViewById(R.id.user_review_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_review, parent, false)
        return UserReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserReviewViewHolder, position: Int) {
        val review = reviewList[position]
        holder.profileImage.setImageResource(review.profileImage)
        holder.userName.text = review.userName
        holder.reviewText.text = review.reviewText
    }

    override fun getItemCount(): Int = reviewList.size
}